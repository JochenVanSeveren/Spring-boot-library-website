package com.hogent.g2a1_vanseveren_jochen;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorTestController {

    @GetMapping("/error-test")
    public String errorTest() {
        throw new RuntimeException("Test error");
    }

}
