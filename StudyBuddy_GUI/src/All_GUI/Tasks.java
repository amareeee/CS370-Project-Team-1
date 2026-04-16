package All_GUI;

import java.time.LocalDate;

//this class contains what a task is, while task manager creates/edits from this class
public class Tasks {
    private String title;
    private String description;
    private boolean completed;
    private LocalDate dueDate;

    //Getters
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public boolean isCompleted() {
        return completed;
    }

    //Setters
    //using setters instead of constructors so we can edit portions of task
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDueDate (LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    //Completing task behavior
    public void markCompleted() {
        this.completed = true;
    }
    public void markIncompleted() {
        this.completed = false;
    }
}
