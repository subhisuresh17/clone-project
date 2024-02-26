package pl.rengreen.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loadingController {

    @GetMapping("/loading")
    public String loadAnimation() {
        return "views/loading";
    }
}
