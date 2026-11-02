package com.DespesasFinanceiras.V1.Controller;

import com.DespesasFinanceiras.V1.Config.TokenConfig;
import com.DespesasFinanceiras.V1.DTOS.request.LoginRequest;
import com.DespesasFinanceiras.V1.DTOS.request.RegisterUserRequest;
import com.DespesasFinanceiras.V1.DTOS.response.LoginResponse;
import com.DespesasFinanceiras.V1.DTOS.response.RegisterUserResponse;
import com.DespesasFinanceiras.V1.Entity.UserEntity;
import com.DespesasFinanceiras.V1.Service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        UserEntity user = (UserEntity)  authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));

    }
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest registerUserRequest){
        userService.createUser(registerUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(registerUserRequest.name(),registerUserRequest.email()));

    }
}
