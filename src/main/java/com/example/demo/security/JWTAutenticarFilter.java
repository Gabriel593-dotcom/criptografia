package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.data.DetalheUsuarioData;
import com.example.demo.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final int TOKEN_EXPIRATION = 600000;
	public static final String TOKEN_SENHA = "64859c55-e345-4dd5-bea2-86b8a115f526";

	public JWTAutenticarFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			Usuario obj = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(obj.getLogin(), obj.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException("falha na autenticação. " + e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		DetalheUsuarioData usuarioData = (DetalheUsuarioData) authResult.getPrincipal();
		String token = JWT.create().withSubject(usuarioData.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
				.sign(Algorithm.HMAC512(TOKEN_SENHA));
		response.getWriter().write(token);
		response.getWriter().flush();
	}

}
