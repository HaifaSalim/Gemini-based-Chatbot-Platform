package com.chatbot;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chatbot.JwtUtil;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		System.out.println("JWT Filter- Processing request: " + requestURI);

		final String authorizationHeader = request.getHeader("Authorization");
		System.out.println("Authorization header: " + (authorizationHeader != null
				? authorizationHeader.substring(0, Math.min(20, authorizationHeader.length())) + "..."
				: "null"));

		String email = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			System.out.println("JWT token extracted: " + jwt.substring(0, Math.min(20, jwt.length())) + "...");
			try {
				email = jwtUtil.extractEmail(jwt);
				System.out.println("Email extracted from token: " + email);
			} catch (Exception e) {
				System.out.println("Error extracting email from token: " + e.getMessage());
			}
		} else {
			System.out.println("No Bearer token found in Authorization header");
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			System.out.println("Validating token for email: " + email);
			if (jwtUtil.isValidToken(jwt)) {
				System.out.println("Token is valid");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null,
						new ArrayList<>());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				System.out.println("Token validation failed");
			}
		} else if (email == null) {
			System.out.println("No email extracted from token");
		} else {
			System.out.println("Authentication already exists in context");
		}

		System.out.println("Current authentication: " + (SecurityContextHolder.getContext().getAuthentication() != null
				? SecurityContextHolder.getContext().getAuthentication().getName()
				: "null"));
		System.out.println("---");

		filterChain.doFilter(request, response);
	}
}