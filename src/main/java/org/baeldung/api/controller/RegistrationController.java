package org.baeldung.api.controller;

import com.nimbusds.jose.util.Base64URL;
import org.baeldung.api.dto.TokenDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang Wenlin
 */
@RestController("registrationApi")
@RequestMapping(value = "/api")
public class RegistrationController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Hello, %s!", jwt.getSubject());
    }

    @PostMapping("/login")
    public TokenDto login(@RequestParam String username, @RequestParam String password) {
        String header = "{'alg':'none'}".replace('\'', '"');
        String scope = "oper".equals(username) ? "oper" : "";
        String payload = String.format("{'sub':'%s','scope':'%s','iss':'baeldung'}", username, scope).replace('\'', '"');
        String token = Base64URL.encode(header).toString()
                + '.' + Base64URL.encode(payload).toString()
                + '.';
        return new TokenDto(token, token);
    }

    @GetMapping("/logout")
    public String logout(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Good bye, %s!", jwt.getSubject());
    }

}
