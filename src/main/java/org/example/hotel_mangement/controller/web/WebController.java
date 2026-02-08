package org.example.hotel_mangement.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @RequestMapping({"/", "/index", "/index.html"})
    public String index() {
        return "redirect:/web/dashboard";
    }

    @RequestMapping("/welcome")
    public String greetingPage(Model model) {
        model.addAttribute("name", "Welcome to Hotel Management!");
        return "index.html";
    }
    
    @RequestMapping("/params/{name}")
    public String greeting(Model model, @RequestParam(value = "name", defaultValue = "Hello") String name){
        model.addAttribute("name", "Welcome to Hotel Management!");
        return "index.html";
    }
}