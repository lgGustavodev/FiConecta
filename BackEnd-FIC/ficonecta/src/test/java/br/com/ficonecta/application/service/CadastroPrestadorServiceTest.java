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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastroPrestadorServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestadorRepository prestadorRepository;

    @Mock
    private PrestadorMapper prestadorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CadastroPrestadorService cadastroPrestadorService;


    @Test
    void deveCadastrarPrestadorComSucesso() {

        // Arrange
        CadastroPrestadorRequest request = CadastroPrestadorRequest.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("123456")
                .cpf("12345678900")
                .descricao("Repositor de estoque")
                .build();

        Usuario usuarioSalvo = Usuario.builder()
                .id(UUID.randomUUID())
                .email("joao@email.com")
                .senha("senha-criptografada")
                .tipoUsuario(TipoUsuario.PRESTADOR)
                .status(StatusUsuario.ATIVO)
                .build();

        Prestador prestador = Prestador.builder()
                .nome("João Silva")
                .cpf("12345678900")
                .descricao("Repositor de estoque")
                .build();

        PrestadorResponse response = PrestadorResponse.builder()
                .id(UUID.randomUUID())
                .nome("João Silva")
                .email("joao@email.com")
                .cpf("12345678900")
                .descricao("Repositor de estoque")
                .disponivel(true)
                .build();

        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(prestadorRepository.existsByCpf(request.getCpf()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getSenha()))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        when(prestadorMapper.toEntity(request))
                .thenReturn(prestador);

        when(prestadorRepository.save(any(Prestador.class)))
                .thenReturn(prestador);

        when(prestadorMapper.toResponse(prestador))
                .thenReturn(response);


        // Act
        PrestadorResponse resultado =
                cadastroPrestadorService.cadastrar(request);


        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertEquals("12345678900", resultado.getCpf());

        verify(passwordEncoder).encode("123456");

        verify(usuarioRepository).save(any(Usuario.class));
        verify(prestadorRepository).save(any(Prestador.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {

        // Arrange
        CadastroPrestadorRequest request = CadastroPrestadorRequest.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("123456")
                .cpf("12345678900")
                .descricao("Repositor de estoque")
                .build();

        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                EmailJaCadastradoException.class,
                () -> cadastroPrestadorService.cadastrar(request)
        );

        verify(usuarioRepository)
                .existsByEmail(request.getEmail());

        verify(usuarioRepository, never())
                .save(any(Usuario.class));

        verify(prestadorRepository, never())
                .save(any(Prestador.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaEstiverCadastrado() {

        // Arrange
        CadastroPrestadorRequest request = CadastroPrestadorRequest.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("123456")
                .cpf("12345678900")
                .descricao("Repositor de estoque")
                .build();

        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(prestadorRepository.existsByCpf(request.getCpf()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                CpfJaCadastradoException.class,
                () -> cadastroPrestadorService.cadastrar(request)
        );

        // Assert
        verify(usuarioRepository)
                .existsByEmail(request.getEmail());

        verify(prestadorRepository)
                .existsByCpf(request.getCpf());

        verify(usuarioRepository, never())
                .save(any(Usuario.class));

        verify(prestadorRepository, never())
                .save(any(Prestador.class));
    }

    @Test
    void deveCadastrarPrestadorQuandoCpfForNulo() {

        // Arrange
        CadastroPrestadorRequest request = CadastroPrestadorRequest.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("123456")
                .cpf(null)
                .descricao("Repositor de estoque")
                .build();

        Usuario usuarioSalvo = Usuario.builder()
                .id(UUID.randomUUID())
                .email("joao@email.com")
                .senha("senha-criptografada")
                .tipoUsuario(TipoUsuario.PRESTADOR)
                .status(StatusUsuario.ATIVO)
                .build();

        Prestador prestador = Prestador.builder()
                .nome("João Silva")
                .cpf(null)
                .descricao("Repositor de estoque")
                .build();

        PrestadorResponse response = PrestadorResponse.builder()
                .id(UUID.randomUUID())
                .nome("João Silva")
                .email("joao@email.com")
                .build();

        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getSenha()))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        when(prestadorMapper.toEntity(request))
                .thenReturn(prestador);

        when(prestadorRepository.save(any(Prestador.class)))
                .thenReturn(prestador);

        when(prestadorMapper.toResponse(prestador))
                .thenReturn(response);

        // Act
        PrestadorResponse resultado =
                cadastroPrestadorService.cadastrar(request);

        // Assert
        assertNotNull(resultado);

        verify(prestadorRepository, never())
                .existsByCpf(anyString());
    }


}