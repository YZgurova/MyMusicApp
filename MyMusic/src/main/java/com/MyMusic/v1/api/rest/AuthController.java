package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.LoginInput;
import com.MyMusic.v1.api.rest.models.RegisterInput;
import com.MyMusic.v1.api.rest.models.AuthenticationDTO;
import com.MyMusic.v1.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", consumes = {"application/json"})
    public AuthenticationDTO login(@RequestBody LoginInput input) {
        return authService.login(input.username, input.password);
    }

    @PostMapping(value = "/register", consumes = {"application/json"})
    public AuthenticationDTO register(@RequestBody RegisterInput input) {
        authService.register(input.firstName, input.lastName, input.username, input.password, input.email);
        return authService.login(input.username, input.password);
    }
}
