package br.com.ficonecta.adapter.output.persistence.mapper;

import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.domain.model.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "foto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Empresa toEntity(CadastroEmpresaRequest request);

    @Mapping(target = "email", source = "usuario.email")
    EmpresaResponse toResponse(Empresa empresa);
}