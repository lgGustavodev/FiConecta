package br.com.ficonecta.adapter.input.web;

import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.application.service.CadastroEmpresaService;
import br.com.ficonecta.application.service.CadastroPrestadorService;
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
public class EmpresaController {

    private final CadastroEmpresaService cadastroEmpresaService;

    private  final EmpresaPerfilService empresaPerfilService;
    private final AutenticacaoUtil autenticacaoUtil;


    @PostMapping("/cadastro")
    public ResponseEntity<EmpresaResponse> cadastrarEmpresa(
            @Valid @RequestBody CadastroEmpresaRequest request) {

        EmpresaResponse response = cadastroEmpresaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public EmpresaResponse meuPerfil() {
        return empresaPerfilService.buscarMeuPerfil(autenticacaoUtil.getUsuarioIdAutenticado());
    }

}
