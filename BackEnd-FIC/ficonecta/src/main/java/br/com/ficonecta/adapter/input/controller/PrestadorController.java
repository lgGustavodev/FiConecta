package br.com.ficonecta.adapter.input.controller;

import br.com.ficonecta.adapter.input.controller.swagger.PrestadorControllerSwagger;
import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.PrestadorDashboardResponse;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.application.service.CadastroPrestadorService;
import br.com.ficonecta.application.service.PrestadorDashboardService;
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
public class PrestadorController implements PrestadorControllerSwagger {

    private final CadastroPrestadorService cadastroPrestadorService;
    private  final PrestadorPerfilService prestadorPerfilService;
    private final AutenticacaoUtil autenticacaoUtil;
    private final PrestadorDashboardService prestadorDashboardService;


    @PostMapping("/cadastro")
    @Override
    public ResponseEntity<PrestadorResponse> cadastrarPrestador(
            @Valid @RequestBody CadastroPrestadorRequest cadastroPrestadorRequest){
        PrestadorResponse response = cadastroPrestadorService.cadastrar(cadastroPrestadorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Override
    public PrestadorResponse meuPerfil() {
        return prestadorPerfilService.buscarMeuPerfil(
                autenticacaoUtil.getUsuarioIdAutenticado()
        );
    }

    @GetMapping("/dashboard")
    @Override
    public PrestadorDashboardResponse dashboard() {
        return prestadorDashboardService.buscarDashboard(
                autenticacaoUtil.getUsuarioIdAutenticado()
        );
    }
}
//http://localhost:8080/api/cadastro-prestadores
