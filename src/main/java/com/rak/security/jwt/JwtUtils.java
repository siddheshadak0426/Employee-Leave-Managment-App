package com.rak.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.rak.security.user.SecurityUser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Step: 2
@Component
public class JwtUtils 
{
	
	private final static Logger logger=LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${auth.token.jwtSecret}")
	private String jwtSecret;
	
	@Value("${auth.token.expirationinMils}")
	private int jwtExpirationMs;
	
	public String generateJwtTokenForUser(Authentication authentication)
	{
		SecurityUser user= (SecurityUser) authentication.getPrincipal();
		
		List<String> roleList=user.getAuthorities().stream().map(authoritiy->authoritiy.getAuthority()).toList();
		
		return Jwts
				.builder()
				.subject(user.getUsername())
				.claim("roleList", roleList)
				.issuedAt(new Date())
				.expiration( new Date( new Date().getTime()+jwtExpirationMs ) )
				.signWith(secrateKey())
				.compact();
		
	}
	
	public Key secrateKey()
	{
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUserNameFromToken(String token)
	{
		return 	Jwts
					.parser()
					.verifyWith((SecretKey) secrateKey())
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject();
		
	}
	
	public boolean validateToken(String token)
	{
		try
		{
			Jwts
				.parser()
				.verifyWith((SecretKey) secrateKey())
				.build()
				.parse(token);
			return true;
		}
		catch(MalformedJwtException e)
		{
			logger.error("Invalid jwt token : {} ", e.getMessage());
		}
		catch(ExpiredJwtException e)
		{
			logger.error("Expired token : {} ", e.getMessage());
		}
		catch (UnsupportedJwtException e)
        {
            logger.error("This token is not supported : {} ", e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            logger.error("No  claims found : {} ", e.getMessage());
        }
		
		return false;
	}
	
}
