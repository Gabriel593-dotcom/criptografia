package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Usuario;
import com.example.demo.services.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@GetMapping("/listarTodos")
	public ResponseEntity<List<Usuario>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@PostMapping("/salvarUsuario")
	public ResponseEntity<Usuario> save(@RequestBody Usuario obj) {
		service.save(obj);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping("/validarSenha")
	public ResponseEntity<Boolean> passwordValidation(@RequestParam String login, @RequestParam String password) {
		boolean isMatched = service.passwordValidation(login, password);
		HttpStatus status = (isMatched) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		return ResponseEntity.status(status).body(isMatched);
	}
}
