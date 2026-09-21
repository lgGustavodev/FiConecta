package br.com.ficonecta.adapter.input.web;

import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.application.service.CadastroPrestadorService;
import br.com.ficonecta.application.service.PrestadorPerfilService;
import br.com.ficonecta.infrastructure.security.AutenticacaoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prestadores")
@RequiredArgsConstructor
public class PrestadorController {

    private final CadastroPrestadorService cadastroPrestadorService;
    private  final PrestadorPerfilService prestadorPerfilService;
    private final AutenticacaoUtil autenticacaoUtil;


    @PostMapping("/cadastro")
    public ResponseEntity<PrestadorResponse> cadastrarPrestador(
            @Valid @RequestBody CadastroPrestadorRequest cadastroPrestadorRequest){
        PrestadorResponse response = cadastroPrestadorService.cadastrar(cadastroPrestadorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public PrestadorResponse meuPerfil() {
        return prestadorPerfilService.buscarMeuPerfil(
                autenticacaoUtil.getUsuarioIdAutenticado()
        );
    }

}
//http://localhost:8080/api/cadastro-prestadores