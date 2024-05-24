package com.sproutt.symposium;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/symposium")
public class SymposiumController {

	private final SymposiumService symposiumService;

	public SymposiumController(SymposiumService symposiumService) {
		this.symposiumService = symposiumService;
	}

	@GetMapping
	public String test() {
		return symposiumService.execute();
	}
}
