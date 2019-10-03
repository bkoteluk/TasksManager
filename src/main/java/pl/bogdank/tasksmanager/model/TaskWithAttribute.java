package pl.bogdank.tasksmanager.model;

import pl.bogdank.tasksmanager.entity.Task;

public class TaskWithAttribute {
    Task task;
    boolean afterNow;
    long duration;

    public TaskWithAttribute(Task task, boolean afterNow, long duration) {
        this.task = task;
        this.afterNow = afterNow;
        this.duration = duration;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isAfterNow() {
        return afterNow;
    }

    public void setAfterNow(boolean afterNow) {
        this.afterNow = afterNow;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
