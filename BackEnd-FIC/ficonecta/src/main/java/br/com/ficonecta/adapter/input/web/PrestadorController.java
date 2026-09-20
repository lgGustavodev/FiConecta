package br.com.ficonecta.adapter.input.web;

import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
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
@RequestMapping("/cadastro-prestadores")
@RequiredArgsConstructor
public class PrestadorController {

    private final CadastroPrestadorService cadastroPrestadorService;

    @PostMapping
    public ResponseEntity<PrestadorResponse> cadastrarPrestador(
            @Valid @RequestBody CadastroPrestadorRequest cadastroPrestadorRequest){
        PrestadorResponse response = cadastroPrestadorService.cadastrar(cadastroPrestadorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
//http://localhost:8080/api/cadastro-prestadores