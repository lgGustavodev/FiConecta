package br.com.ficonecta.application.service;

import br.com.ficonecta.adapter.output.persistence.mapper.EmpresaMapper;
import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.domain.model.Empresa;
import br.com.ficonecta.domain.model.StatusUsuario;
import br.com.ficonecta.domain.model.TipoUsuario;
import br.com.ficonecta.domain.model.Usuario;
import br.com.ficonecta.domain.port.output.EmpresaRepository;
import br.com.ficonecta.domain.port.output.UsuarioRepository;
import br.com.ficonecta.infrastructure.exception.DocumentoJaCadastradoException;
import br.com.ficonecta.infrastructure.exception.EmailJaCadastradoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroEmpresaService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EmpresaResponse cadastrar(CadastroEmpresaRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailJaCadastradoException(request.getEmail());
        }

        if (request.getDocumento() != null && !request.getDocumento().isBlank()
                && empresaRepository.existsByDocumento(request.getDocumento())) {
            throw new DocumentoJaCadastradoException(request.getDocumento());
        }

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipoUsuario(TipoUsuario.EMPRESA)
                .status(StatusUsuario.ATIVO)
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Empresa empresa = empresaMapper.toEntity(request);
        empresa.setUsuario(usuarioSalvo);

        Empresa empresaSalva = empresaRepository.save(empresa);

        return empresaMapper.toResponse(empresaSalva);
    }

}
