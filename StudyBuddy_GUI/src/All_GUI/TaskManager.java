package All_GUI;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Tasks> tasks = new ArrayList<>();
    private GoogleCalendarSync calendarSync;
    private Healthbar healthbar;
    private BuddyPage buddyPage;

    //EDIT: constructor takes Healthbar so TaskManager can modify hp whenever tasks are completed
    public TaskManager(Healthbar healthbar, BuddyPage buddyPage) {
        this.healthbar = healthbar;
        this.buddyPage = buddyPage;
    }

    public void setCalendarSync(GoogleCalendarSync calendarSync) {
        this.calendarSync = calendarSync;
    }

    public void addTask(Tasks task) {
        tasks.add(task);

        if (calendarSync != null && calendarSync.isConnected()) {
            calendarSync.syncTask(task);
        }
    }

    public void removeTask(Tasks task) {
        tasks.remove(task);
        if (calendarSync != null && calendarSync.isConnected()) {
            calendarSync.deleteTaskEvent(task);
        }
    }
        public List<Tasks> getAllTasks () {
            return tasks;
        }

        //gets called when user marks task complete
        //checks if task was completed on time and adjusts buddy hp
        public void completeTask(Tasks task){
            task.setCompleted(true); //marks task done
            int current = healthbar.getCurrentHealth(); //gets buddy's current gp


            if (task.CompletedOnTime()) {
                healthbar.setHealth(current + 10); //complete on time -> +10% hp
            } else {
                healthbar.setHealth(current - 10); //late completion -> -10% hp
            }
            //checks if creature img needs to switch whenever hp changes
            buddyPage.updateImg(healthbar.getCurrentHealth());

            if(calendarSync != null && calendarSync.isConnected()) {
                calendarSync.syncTask(task);
        }
    }
}