package com.rak.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rak.security.user.SecurityUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// Step: 3
@Component
public class JwtOncePerRequestFilter extends OncePerRequestFilter
{
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private SecurityUserDetailsService securityUserDetailsService;
	
	private final static Logger logger=LoggerFactory.getLogger(JwtOncePerRequestFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
	{
		try
		{
			logger.info("in oncePerRequestFilter class doFilterInternal() ...!!!");
			String jwt=parseJwt(request);
			logger.info("jwt: {} ", jwt);
			
			if(jwt!=null && jwtUtils.validateToken(jwt))
			{
				String email=jwtUtils.getUserNameFromToken(jwt);
				UserDetails userDetails=securityUserDetailsService.loadUserByUsername(email);
				
				UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		catch(Exception e)
		{
			logger.error("cannot set user Authentication : {} ", e.getMessage());
		}
		
		filterChain.doFilter(request, response); // ***VIP***
	}
	
	private String parseJwt(HttpServletRequest request)
	{
		String authorizationHeader=request.getHeader("Authorization");
		if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer "))
		{
			logger.info("jwt token: {} ", authorizationHeader.substring(7));
			return authorizationHeader.substring(7);
		}
		
		return null;
	}
	
}
