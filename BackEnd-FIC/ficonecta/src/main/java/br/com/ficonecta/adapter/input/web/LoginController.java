package br.com.ficonecta.adapter.input.web;

import br.com.ficonecta.application.dto.request.LoginRequest;
import br.com.ficonecta.application.dto.response.LoginResponse;
import br.com.ficonecta.application.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginService.autenticar(request);
        return ResponseEntity.ok(response);
    }
}
