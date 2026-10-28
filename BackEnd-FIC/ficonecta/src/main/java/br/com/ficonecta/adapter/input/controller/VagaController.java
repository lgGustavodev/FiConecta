package br.com.ficonecta.adapter.input.controller;


import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.application.service.CadastroVagaService;
import br.com.ficonecta.infrastructure.security.AutenticacaoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/empresas/vagas")
@RequiredArgsConstructor
public class VagaController {

    private final CadastroVagaService cadastroVagaService;
    private final AutenticacaoUtil autenticacaoUtil;

    @PostMapping
    public ResponseEntity<VagaResponse> cadastrar(@Valid @RequestBody CadastroVagaRequest request){
        UUID usuarioId = autenticacaoUtil.getUsuarioIdAutenticado();
        VagaResponse response = cadastroVagaService.cadastrar(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
