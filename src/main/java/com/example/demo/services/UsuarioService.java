package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repo;

	private final PasswordEncoder encoder;

	public UsuarioService(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

	public List<Usuario> findAll() {
		return repo.findAll();
	}

	public Optional<Usuario> findByLogin(String login) {
		return repo.findByLogin(login);
	}

	public Usuario save(Usuario obj) {
		obj.setId(null);
		obj.setPassword(encoder.encode(obj.getPassword()));
		repo.save(obj);
		return obj;
	}
	
	public boolean passwordValidation(String login, String password) {
		Optional<Usuario> obj = repo.findByLogin(login);
		if(obj.isEmpty()) {
			throw new IllegalArgumentException("Usuario inválido");
		}
		boolean isMatched = encoder.matches(password, obj.get().getPassword());
		return isMatched; 
	}
}
