package com.rak.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rak.security.jwt.JwtAuthenticationEntryPoint;
import com.rak.security.jwt.JwtOncePerRequestFilter;
import com.rak.security.user.SecurityUserDetailsService;

// Step: 4
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig 
{
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private SecurityUserDetailsService securityUserDetailsService;
	
	@Autowired
	private JwtOncePerRequestFilter jwtOncePerRequestFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		return	http
					.csrf(csrf->csrf.disable())
					.exceptionHandling(exception->exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
					.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.authorizeHttpRequests(auth->auth.requestMatchers("/auth/**").permitAll())
					.authorizeHttpRequests(auth->auth.requestMatchers("/employee/**", "/admin/**").authenticated())
					.authenticationProvider(authenticationProvider())
					.addFilterBefore(jwtOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class)
//					.cors(cors->cors.configurationSource(new CorsConfigurationSource() 
//						{
//							@Override
//							public CorsConfiguration getCorsConfiguration(HttpServletRequest request) 
//							{
//								CorsConfiguration cfg=new CorsConfiguration();
//								
//								cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4000", "http://localhost:5000")); // Requests are allowed only from specific domains (localhost:3000, localhost:4000, localhost:5000).
//		                        //cfg.setAllowedMethods(Arrays.asList("GET", "POST","DELETE","PUT"));
//								cfg.setAllowedMethods(Collections.singletonList("*")); // allows all/any HTTP methods (e.g., GET, POST, PUT, DELETE, OPTIONS, etc.)
//								cfg.setAllowCredentials(true); // Cookies and credentials can be sent with the requests.
//								cfg.setAllowedHeaders(Collections.singletonList("*")); // Any header can be sent by the client (because of * wildcard).
//								cfg.setExposedHeaders(Arrays.asList("Authorization")); // The Authorization header can be accessed by the client in the response.
//								cfg.setMaxAge(3600L); // Pre flight responses are cached for 1 hour to optimize performance.
//								
//								return cfg;
//							}
//						}))
					.build();
		
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(securityUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
	{
		return authConfig.getAuthenticationManager();
	}
	
	
}
