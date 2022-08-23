package com.sally.api.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/{team-name}")
@RestController
public class ApiController {

	@GetMapping("/issues/{id}")
	public ResponseEntity<List<String>> test(@PathVariable("team-name") String teamName, @PathVariable("id") Long issueId) {
		System.out.println("teamName = " + teamName + ", issueId = " + issueId);
		return ResponseEntity.ok().body(List.of(teamName, String.valueOf(issueId)));
	}
}
