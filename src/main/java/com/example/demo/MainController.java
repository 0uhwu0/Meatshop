package com.example.demo;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.*;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController {

	@GetMapping
    public String mainPage() {
		return "main";
    }
}
