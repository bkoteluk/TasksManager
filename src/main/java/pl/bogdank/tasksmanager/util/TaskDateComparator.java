package pl.bogdank.tasksmanager.util;

import pl.bogdank.tasksmanager.model.Task;

import java.util.Comparator;

public class TaskDateComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if(o1.getStartDate().isBefore(o2.getStartDate())) {
            return -1;
        } else if (o1.getStartDate().isAfter(o2.getStartDate())) {
            return 1;
        } else
            return 0;

    }
}
