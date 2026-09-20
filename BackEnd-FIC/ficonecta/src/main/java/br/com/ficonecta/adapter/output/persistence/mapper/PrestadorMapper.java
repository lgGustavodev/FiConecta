package br.com.ficonecta.adapter.output.persistence.mapper;

import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.domain.model.Prestador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestadorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "disponivel", ignore = true)
    @Mapping(target = "foto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Prestador toEntity(CadastroPrestadorRequest request);

    @Mapping(target = "email", source = "usuario.email")
    PrestadorResponse toResponse(Prestador prestador);
}