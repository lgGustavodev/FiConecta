package br.com.ficonecta.adapter.input.controller;


import br.com.ficonecta.adapter.input.controller.swagger.VagaControllerSwagger;
import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.response.PaginaResponse;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.application.service.CadastroVagaService;
import br.com.ficonecta.application.service.EmpresaVagaService;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.infrastructure.security.AutenticacaoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/empresas/vagas")
@RequiredArgsConstructor
public class VagaController implements VagaControllerSwagger {

    private final CadastroVagaService cadastroVagaService;
    private final AutenticacaoUtil autenticacaoUtil;
    private final EmpresaVagaService empresaVagaService;


    @PostMapping
    @Override
    public ResponseEntity<VagaResponse> cadastrar(@Valid @RequestBody CadastroVagaRequest request){
        UUID usuarioId = autenticacaoUtil.getUsuarioIdAutenticado();
        VagaResponse response = cadastroVagaService.cadastrar(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // novo método
    @GetMapping
    @Override
    public PaginaResponse<VagaResponse> listar(
            @RequestParam(required = false) StatusVaga status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        UUID usuarioId = autenticacaoUtil.getUsuarioIdAutenticado();
        return empresaVagaService.listar(usuarioId, status, page, size);
    }
}
