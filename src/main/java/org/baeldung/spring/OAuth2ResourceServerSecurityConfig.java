package org.baeldung.spring;

import com.nimbusds.jose.Header;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.text.ParseException;
import java.time.Instant;

@Configuration
@Order(50)
public class OAuth2ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.antMatcher("/api/**")
				.csrf().disable()
				.authorizeRequests(authorizeRequests ->
						authorizeRequests
								.antMatchers("/*/login*", "/*/signup*", "/*/refresh*").permitAll()
								.antMatchers("/*/oper/**").hasAuthority("SCOPE_oper")
								.anyRequest().authenticated()
				)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.oauth2ResourceServer(oauth2ResourceServer ->
						oauth2ResourceServer
								.jwt(jwt ->
										jwt.decoder(insecureJwtDecoder())
								)
				);
		// @formatter:on
	}

	@Bean("jwtDecoder")
	JwtDecoder insecureJwtDecoder() {
		return token -> {
			try {
				JWT parsedJwt = JWTParser.parse(token);
				Header header = parsedJwt.getHeader();
				JWTClaimsSet claims = parsedJwt.getJWTClaimsSet();
				Instant issueAt = Instant.now();
				Instant expiresAt = Instant.MAX;
				return new Jwt(token, issueAt, expiresAt, header.toJSONObject(), claims.toJSONObject());
			} catch (ParseException | NullPointerException e) {
				throw new JwtException("Malformed JWT token", e);
			}
		};
	}

}
