package pl.bogdank.tasksmanager.util;

import pl.bogdank.tasksmanager.entity.Task;
import pl.bogdank.tasksmanager.model.TaskWithAttribute;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class TaskAttributes {
    public static List<TaskWithAttribute> addTasksAttributes(List<Task> tasks) {
        List<TaskWithAttribute> tasksWithAttribute = new ArrayList<>();
        for (Task task : tasks) {
            long duration = -1;
            if (task.getEndDate() != null)
                duration = DAYS.between(task.getStartDate(), task.getEndDate());

            if (task.getStartDate().isAfter(LocalDate.now())) {
                tasksWithAttribute.add(new TaskWithAttribute(task,false, duration));
            } else
                tasksWithAttribute.add(new TaskWithAttribute(task,true, duration));
        }
        return tasksWithAttribute;
    }
}
