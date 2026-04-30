package All_GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskManager {
    private List<Tasks> tasks = new ArrayList<>();
    private Healthbar healthbar;
    private BuddyPage buddyPage;

    //EDIT: constructor takes Healthbar so TaskManager can modify hp whenever tasks are completed
    public TaskManager(Healthbar healthbar, BuddyPage buddyPage) {
        this.healthbar = healthbar;
        this.buddyPage = buddyPage;
    }

    public void addTask(Tasks task) {
        tasks.add(task);

        //immediately checks if task created is overdue
        if (task.isOverdue() && !task.isPenalty()) {
            int curr = healthbar.getCurrentHealth();
            healthbar.setHealth(curr - 10); //overdue task -> -10% hp
            buddyPage.updateImg(healthbar.getCurrentHealth());
            task.setPenalty(true);
        }
    }
    public void removeTask(Tasks task) {
        tasks.remove(task);
    }
    public List<Tasks> getAllTasks() {
        return tasks;
    }

    //gets called when user marks task complete
    //checks if task was completed on time and adjusts buddy hp
    public void completeTask(Tasks task) {
        task.setCompleted(true); //marks task done
        int current = healthbar.getCurrentHealth(); //gets buddy's current gp

        if (task.CompletedOnTime()) {
            healthbar.setHealth(current + 10); //complete on time -> +10% hp
        } else {
            healthbar.setHealth(current + 5); //late completion -> +5% hp
        }
        //checks if creature img needs to switch whenever hp changes
        buddyPage.updateImg(healthbar.getCurrentHealth());
    }

    public void OverdueCheck() {
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Tasks task : tasks) {
                    if (task.isOverdue() && !task.isPenalty()) {
                        int curr = healthbar.getCurrentHealth();
                        healthbar.setHealth(curr - 10);; //overdue task present -> -10% hp
                        buddyPage.updateImg(healthbar.getCurrentHealth());

                        task.setPenalty(true);
                    }
                }
            }
        }, 0, 86400000); //runs check immediately when task is created
    }
}
