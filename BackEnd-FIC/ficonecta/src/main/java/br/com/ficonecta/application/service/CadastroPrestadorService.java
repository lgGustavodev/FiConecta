package br.com.ficonecta.application.service;

import br.com.ficonecta.adapter.output.persistence.mapper.PrestadorMapper;
import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.domain.model.Prestador;
import br.com.ficonecta.domain.model.StatusUsuario;
import br.com.ficonecta.domain.model.TipoUsuario;
import br.com.ficonecta.domain.model.Usuario;
import br.com.ficonecta.domain.port.output.PrestadorRepository;
import br.com.ficonecta.domain.port.output.UsuarioRepository;
import br.com.ficonecta.infrastructure.exception.CpfJaCadastradoException;
import br.com.ficonecta.infrastructure.exception.EmailJaCadastradoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroPrestadorService {

    private final UsuarioRepository usuarioRepository;
    private final PrestadorRepository prestadorRepository;
    private final PrestadorMapper prestadorMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PrestadorResponse cadastrar(CadastroPrestadorRequest request) {

        validarEmail(request.getEmail());
        validarCpf(request.getCpf());

        Usuario usuario = criarUsuario(request);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Prestador prestador = prestadorMapper.toEntity(request);
        prestador.setUsuario(usuarioSalvo);

        Prestador prestadorSalvo = prestadorRepository.save(prestador);

        return prestadorMapper.toResponse(prestadorSalvo);
    }

    private Usuario criarUsuario(CadastroPrestadorRequest request) {

        return Usuario.builder()
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipoUsuario(TipoUsuario.PRESTADOR)
                .status(StatusUsuario.ATIVO)
                .build();
    }

    private void validarEmail(String email) {

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(email);
        }
    }

    private void validarCpf(String cpf) {

        if (cpf != null && !cpf.isBlank()
                && prestadorRepository.existsByCpf(cpf)) {
            throw new CpfJaCadastradoException(cpf);
        }
    }
}