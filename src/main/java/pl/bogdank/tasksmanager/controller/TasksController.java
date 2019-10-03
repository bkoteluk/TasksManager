package pl.bogdank.tasksmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bogdank.tasksmanager.model.TaskWithAttribute;
import pl.bogdank.tasksmanager.entity.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static pl.bogdank.tasksmanager.util.TaskAttributes.addTasksAttributes;

@Controller
public class TasksController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @GetMapping("/")
    public String home(Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t order by t.startDate", Task.class);
        List<Task> tasks = query.getResultList();

        model.addAttribute("tasksWithAttribute", addTasksAttributes(tasks));

        return "home";
    }

    @GetMapping("/todo")
    public String toDoList(Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t  WHERE (t.endDate IS NULL) order by t.startDate", Task.class);
        List<Task> tasks = query.getResultList();

        model.addAttribute("tasksWithAttribute", addTasksAttributes(tasks));

        return "todolist";
    }

    @GetMapping("/archive")
    public String archiveList(Model model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t WHERE t.isOpen = false order by t.startDate", Task.class);

        List<Task> tasks = query.getResultList();

        model.addAttribute("tasksWithAttribute", addTasksAttributes(tasks));

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
        model.addAttribute("id", id);
        return "edittask";
    }

    @PostMapping(value = "/edit", params = "id")
    public String saveChangedTask(@RequestParam("id") long id, Task taskModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Task task = entityManager.find(Task.class, id);
        task.setTitle(taskModel.getTitle());
        task.setDescription(taskModel.getDescription());
        task.setCategory(taskModel.getCategory());
        task.setStartDate(taskModel.getStartDate());
        task.setEndDate(taskModel.getEndDate());
        entityManager.getTransaction().commit();
        return "redirect:/";
    }

    @GetMapping("/archivetask")
    public String editTask(@RequestParam Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Task task = entityManager.find(Task.class, id);
        task.setOpen(false);
        entityManager.getTransaction().commit();
        return "redirect:/archive";
    }

}
