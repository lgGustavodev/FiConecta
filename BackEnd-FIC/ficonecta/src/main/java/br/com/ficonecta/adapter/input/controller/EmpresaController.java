package br.com.ficonecta.adapter.input.controller;

import br.com.ficonecta.adapter.input.controller.swagger.EmpresaControllerSwagger;
import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.response.EmpresaDashboardResponse;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.application.service.CadastroEmpresaService;
import br.com.ficonecta.application.service.EmpresaDashboardService;
import br.com.ficonecta.application.service.EmpresaPerfilService;
import br.com.ficonecta.infrastructure.security.AutenticacaoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController implements EmpresaControllerSwagger {

    private final CadastroEmpresaService cadastroEmpresaService;
    private final EmpresaDashboardService empresaDashboardService;
    private  final EmpresaPerfilService empresaPerfilService;
    private final AutenticacaoUtil autenticacaoUtil;


    @PostMapping("/cadastro")
    @Override
    public ResponseEntity<EmpresaResponse> cadastrarEmpresa(
            @Valid @RequestBody CadastroEmpresaRequest request) {

        EmpresaResponse response = cadastroEmpresaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Override
    public EmpresaResponse meuPerfil() {
        return empresaPerfilService.buscarMeuPerfil(autenticacaoUtil.getUsuarioIdAutenticado());
    }

    @GetMapping("/dashboard")
    @Override
    public EmpresaDashboardResponse dashboard() {
        return empresaDashboardService.buscarDashboard(
                autenticacaoUtil.getUsuarioIdAutenticado()
        );
    }

}
