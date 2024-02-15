package pl.rengreen.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboardView() {
        return "views/dashboard";
    }

    @GetMapping("/calendar")
    public String calendarView() {
        return "views/calendar";
    }
}
