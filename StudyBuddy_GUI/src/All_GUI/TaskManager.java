package All_GUI;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Tasks> tasks = new ArrayList<>();

    public void addTask(Tasks task) {
        tasks.add(task);
    }
    public void removeTask(Tasks task) {
        tasks.remove(task);
    }
    public List<Tasks> getAllTasks() {
        return tasks;
    }
}
