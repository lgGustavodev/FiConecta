package br.com.ficonecta.adapter.input.web;

import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.application.service.CadastroEmpresaService;
import br.com.ficonecta.application.service.CadastroPrestadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cadastro-empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final CadastroEmpresaService cadastroEmpresaService;


    @PostMapping
    public ResponseEntity<EmpresaResponse> cadastrarEmpresa(
            @Valid @RequestBody CadastroEmpresaRequest request) {

        EmpresaResponse response = cadastroEmpresaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
