package com.hogent.g2a1_vanseveren_jochen;

import com.hogent.g2a1_vanseveren_jochen.Name;
import domain.BoekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class BoekController {

    @Autowired
    private BoekService boekService;


    @GetMapping("/hello")
    public String showFormPage(Model model) {
        model.addAttribute("name", new Name());
        log.info("Get hello");
        return "nameForm";
    }

    @PostMapping("/hello")
    public String onSubmit(Name name, Model model) {
        log.info("post hello, Name submitted: {}", name!=null? name.getValue(): "null");

        model.addAttribute("helloMessage", boekService.sayHello(name.getValue()));
        return "helloView";
    }

}
