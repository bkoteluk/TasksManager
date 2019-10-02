package pl.bogdank.tasksmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.bogdank.tasksmanager.model.TaskWithAttribute;
import pl.bogdank.tasksmanager.model.Task;
import pl.bogdank.tasksmanager.util.TaskDateComparator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;


@Controller
public class TasksController {


    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @GetMapping("/")
    public String home(Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<TaskWithAttribute> tasksWithAttribute = new ArrayList<>();
        TaskDateComparator dateComparator = new TaskDateComparator();
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t", Task.class);
        List<Task> tasks = query.getResultList();

        Collections.sort(tasks, dateComparator);

        for (Task task : tasks) {
            long duration = -1;
            if (task.getEndDate() != null)
                duration = DAYS.between(task.getStartDate(), task.getEndDate());

            if (task.getStartDate().isAfter(LocalDate.now())) {
                tasksWithAttribute.add(new TaskWithAttribute(task,false, duration));
            } else
                tasksWithAttribute.add(new TaskWithAttribute(task,true, duration));
        }

        model.addAttribute("tasksWithAttribute", tasksWithAttribute);

        return "home";
    }

    @GetMapping("/todo")
    public String toDoList(Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<TaskWithAttribute> tasksWithAttribute = new ArrayList<>();
        TaskDateComparator dateComparator = new TaskDateComparator();
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t WHERE (t.endDate IS NULL) ", Task.class);
        List<Task> tasks = query.getResultList();

        Collections.sort(tasks, dateComparator);

        for (Task task : tasks) {
           if (task.getStartDate().isAfter(LocalDate.now())) {
                tasksWithAttribute.add(new TaskWithAttribute(task,false, -1));
            } else
                tasksWithAttribute.add(new TaskWithAttribute(task,true, -1));
        }

        model.addAttribute("tasksWithAttribute", tasksWithAttribute);

        return "todolist";
    }


    @GetMapping("/archive")
    public String archiveList(Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        LocalDate now = LocalDate.now();
        List<TaskWithAttribute> tasksWithAttribute = new ArrayList<>();
        TaskDateComparator dateComparator = new TaskDateComparator();
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t WHERE ((t.startDate < :today) and (t.endDate IS NOT NULL))", Task.class);
        query.setParameter("today", now);

        List<Task> tasks = query.getResultList();

        Collections.sort(tasks, dateComparator);

        for (Task task : tasks) {
            long duration = -1;
            if (task.getEndDate() != null)
                duration = DAYS.between(task.getStartDate(), task.getEndDate());

            if (task.getStartDate().isAfter(LocalDate.now())) {
                tasksWithAttribute.add(new TaskWithAttribute(task,false, duration));
            } else
                tasksWithAttribute.add(new TaskWithAttribute(task,true, duration));
        }

        model.addAttribute("tasksWithAttribute", tasksWithAttribute);

        return "archivelist";
    }

    @GetMapping("/newtask")
    public String newTaskForm(Model model) {
        model.addAttribute("newTask", new Task());
        return "newtask";
    }

    @PostMapping("/save")
    public String addTask(Task task) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(task);
        entityManager.getTransaction().commit();
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String taskInfo(@RequestParam Long id, Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Task task = entityManager.find(Task.class, id);
        model.addAttribute("task", task);
        return "edittask";
    }

    @PostMapping("/edit")
    public String editTask(Task task) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.getTransaction().commit();
        return "redirect:/";
    }

}
