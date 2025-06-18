package ru.teamsync.projects.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    //О боги эта ручка чисто для сабмита 💀

    @GetMapping("/skill")
    public String getSkill() {
        return """
                {
                  "error": null,
                  "success": true,
                  "data": [
                    {
                      "id": 1,
                      "name": "Java",
                      "description": "Cool tea 🤌🏻"
                    }
                  ]
                }
                """;
    }

}
