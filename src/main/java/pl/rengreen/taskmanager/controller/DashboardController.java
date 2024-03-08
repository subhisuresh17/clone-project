package pl.rengreen.taskmanager.controller;
 
import java.text.DecimalFormat;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
 
import pl.rengreen.taskmanager.dataloader.DashboardData;
import pl.rengreen.taskmanager.service.TaskService;
 
@Controller
public class DashboardController {
 
    private final TaskService taskService;
 
    @Autowired
    public DashboardController(TaskService taskService) {
        this.taskService = taskService;
    }
 
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Count total tasks
        long totalTasks = taskService.countTasks();
 
        // Count completed tasks
        long completedTasks = taskService.countCompletedTasks();
 
        // Calculate remaining tasks
        long remainingTasks = totalTasks - completedTasks;
 
        // Calculate progress
        double progress = (double) completedTasks / totalTasks * 100;
 
        // Add attributes to the model
        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("remainingTasks", remainingTasks);
        model.addAttribute("progress", progress);
 
        // Return the view name
          return "views/dashboard";
    }
       @GetMapping("/dashboard/data")
    @ResponseBody
    public DashboardData getDashboardData() {
        // Count total tasks
        long totalTasks = taskService.countTasks();
 
        // Count completed tasks
        long completedTasks = taskService.countCompletedTasks();
 
        // Calculate remaining tasks
        long remainingTasks = totalTasks - completedTasks;
 
        double progress = totalTasks == 0 ? 0 : (double) completedTasks / totalTasks * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        progress = Double.parseDouble(df.format(progress));
 
        // Create DashboardData object
        DashboardData data = new DashboardData(totalTasks, completedTasks, remainingTasks, progress);
 
        return data;
    }
   
}