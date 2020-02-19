package org.baeldung.api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang Wenlin
 */
@RestController("operationApi")
@RequestMapping(value = "/api/oper")
public class OperationController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Hello, %s!", jwt.getSubject());
    }

    @PutMapping("/block/{username}")
    public Boolean block(@PathVariable String username, @RequestBody Boolean block) {
        return block;
    }

}
