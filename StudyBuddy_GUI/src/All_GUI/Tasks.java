package All_GUI;

import java.time.LocalDate;

//this class contains what a task is, while task manager creates/edits from this class
public class Tasks {
    private String title;
    private String description;
    private boolean completed;
    private LocalDate dueDate;

    //constructor
    public Tasks(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
    }

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

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        if (completed) {
            return "✔ " + title;
        } else {
            return title + "Due Date: " + dueDate;
        }
    }
}

