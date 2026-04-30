package All_GUI;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.security.GeneralSecurityException;
import java.util.*;


public class GoogleCalendarSync {

    private static final String APPLICATION_NAME = "Study Buddy";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_RESOURCE = "/resources/credentials.json";

    //Tokens stored per user under ~/.studybuddy/tokens/<username>/
    private static final Path TOKENS_BASE = Paths.get(System.getProperty("user.home"), ".studybuddy","tokens");

    private Calendar service = null;
    private javax.swing.Timer autoSyncTimer;
    private boolean connected = false;

    //Maps task title
    private final Map<String, String> taskEventIds = new HashMap<>();

    // public API
    public boolean isConnected(){
        return connected;
    }

    /*
     * Attempt OAuth2 flow for the given username.
     * Opens a browser window for the user to sign in with Google.
     * Returns null on success, error message on failure.
     */

    public String connect(String username){
        try{
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = authorize(httpTransport, username);
            service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            connected = true;
            System.out.println("[CalendarSync] connected for user: " + username);
            return null;
        } catch (Exception e){
            System.err.println("[CalendarSync] connect failed for user: " + e.getMessage());
            connected = false;
            service = null;
            return "Could not connect to Google Calendar: " + e.getMessage();
        }
    }

    //disconnect and clear all stored token for user
    public void disconnect(String username){
        connected = false;
        service = null;
        taskEventIds.clear();

        Path tokenDirectory = TOKENS_BASE.resolve(username);
        try{
            if (Files.exists(tokenDirectory)) {
                Files.walk(tokenDirectory).sorted(java.util.Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
            System.out.println("[CalendarSync] Disconnected from Google Calendar: " + username);
        } catch (IOException e){
            System.err.println("[CalendarSync] Disconnection unsuccessful: " + e.getMessage());
        }
    }

    public void syncTask(Tasks task){
        if(!connected || service==null) return;

        try{
            String existingId = taskEventIds.get(task.getTitle());
            if(existingId != null){
                Event existing = service.events().get("primary", existingId).execute();
                updateEventFromTask(existing, task);
                service.events().update("primary", existingId, existing).execute();
                System.out.println("[CalendarSync] Update event: " + task.getTitle());
            } else{
                Event event = buildEventFromTask(task);
                Event created = service.events().insert("primary", event).execute();
                taskEventIds.put(task.getTitle(), created.getId());
                System.out.println("[CalendarSync] Created event: " + task.getTitle());
            }
        } catch (IOException e){
            System.err.println("[CalendarSync] Sync task failed: " + e.getMessage());
        }
    }

    //Sync all tasks to google calendar
    public void syncAllTasks(TaskManager taskManager){
        if(!connected || service==null) return;
        for(Tasks task : taskManager.getAllTasks()){
            syncTask(task);
        }
    }

    public void startAutoSync(TaskManager taskManager){
        //syncs every 5 mins
        autoSyncTimer = new javax.swing.Timer(5 * 60 * 1000, e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<>(){
                @Override protected Void doInBackground() {
                    syncAllTasks(taskManager);
                    return null;
                }
            };
            worker.execute();
        });
        autoSyncTimer.start();
        System.out.println("[CalendarSync] Auto-sync started (every 5 min)");

    }

    public void stopAutoSync(){
        if(autoSyncTimer != null){
            autoSyncTimer.stop();
            System.out.println("[CalendarSync] Auto-sync stopped");
        }
    }

    //delete a task
    public void deleteTaskEvent(Tasks task){
        if(!connected || service==null) return;
        String eventId = taskEventIds.get(task.getTitle());
        if(eventId == null) return;
        try {
            service.events().delete("primary", eventId).execute();
            taskEventIds.remove(task.getTitle());
            System.out.println("[CalendarSync] Deleted task event: " + task.getTitle());
        } catch (IOException e){
            System.err.println("[CalendarSync] Delete failed: " + e.getMessage());
        }
    }

    // private constructors

    private Credential authorize(NetHttpTransport httpTransport, String username)
        throws IOException, GeneralSecurityException {

        InputStream in = GoogleCalendarSync.class.getResourceAsStream(CREDENTIALS_RESOURCE);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_RESOURCE);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        //store token per user
        Path tokenDirectory = TOKENS_BASE.resolve(username);
        Files.createDirectories(tokenDirectory);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(tokenDirectory.toFile())).setAccessType("offline").build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private Event buildEventFromTask(Tasks task){
        Event event = new Event();
        updateEventFromTask(event, task);
        return event;
    }

    private void updateEventFromTask(Event event, Tasks task){
        String summary = (task.isCompleted() ? "✔ " : "") + task.getTitle();
        event.setSummary(summary);

        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            event.setDescription(task.getDescription());
        }

        if (task.getDueDate() != null) {
            String dateStr = task.getDueDate().toString(); // "YYYY-MM-DD"
            EventDateTime start = new EventDateTime().setDate(new DateTime(dateStr));
            EventDateTime end   = new EventDateTime().setDate(new DateTime(dateStr));
            event.setStart(start);
            event.setEnd(end);
        }
    }
}
