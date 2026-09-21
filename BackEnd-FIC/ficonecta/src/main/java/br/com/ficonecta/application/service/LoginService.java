package br.com.ficonecta.application.service;

import br.com.ficonecta.application.dto.request.LoginRequest;
import br.com.ficonecta.application.dto.response.LoginResponse;
import br.com.ficonecta.domain.model.Usuario;
import br.com.ficonecta.domain.port.output.UsuarioRepository;
import br.com.ficonecta.infrastructure.exception.CredenciaisInvalidasException;
import br.com.ficonecta.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse autenticar(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getTipoUsuario());

        return LoginResponse.builder()
                .token(token)
                .tipo(usuario.getTipoUsuario().name())
                .build();
    }
}