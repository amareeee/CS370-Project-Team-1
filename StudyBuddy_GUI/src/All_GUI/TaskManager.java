package All_GUI;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final List<Tasks> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }
    //key abilities
    public void addTask(Tasks task) {
        tasks.add(task);
    }
    public boolean removeTask(Tasks task) {
        return tasks.remove(task);
    }
    public List<Tasks> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    //ability to grab tasks in specific categories
    public List<Tasks> getCompletedTasks() {
        List<Tasks> completed = new ArrayList<>();
        for (Tasks t : tasks) {
            if (t.isCompleted()) {
                completed.add(t);
            }
        }
        return completed;
    }
    public List<Tasks> getPendingTasks() {
        List<Tasks> pending = new ArrayList<>();
        for (Tasks t : tasks) {
            if (!t.isCompleted()) {
                pending.add(t);
            }
        }
        return pending;
    }
}
