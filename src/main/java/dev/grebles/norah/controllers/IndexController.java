package dev.grebles.norah.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String home(Model model) {
        // Add any necessary attributes to the model
        // For example:
        // model.addAttribute("message", "Hello User");

        // Return the name of the HTML file in the templates directory
        return "index"; // Assuming "index.html" is the file you want to serve
    }
}
