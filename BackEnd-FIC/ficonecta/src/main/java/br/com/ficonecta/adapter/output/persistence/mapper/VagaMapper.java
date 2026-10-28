package br.com.ficonecta.adapter.output.persistence.mapper;


import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.request.EnderecoRequest;
import br.com.ficonecta.application.dto.response.EnderecoResponse;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VagaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vaga toEntity(CadastroVagaRequest request);

    VagaResponse toResponse(Vaga vaga);

    Endereco toEndereco(EnderecoRequest request);

    EnderecoResponse toEnderecoResponse(Endereco endereco);
}