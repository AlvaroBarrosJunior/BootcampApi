package com.bootcamp.api.utils;

import java.util.Date;

import com.bootcamp.api.model.Dev;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtils {
	
	private static final long TOKEN_EXPIRATION = 1800000;
	private static final String TOKEN_KEY = "${token.key}";
	
	public static String generateToken(Dev dev) {
		return Jwts.builder()
				.setIssuedAt(new Date())
				.setSubject(dev.getIdDev().toString())
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, TOKEN_KEY).compact();
	}
	
	public static Claims decodeToken(String token) {
		return Jwts.parser()
				.setSigningKey(TOKEN_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

}
