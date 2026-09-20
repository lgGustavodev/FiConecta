package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.Empresa;

public interface EmpresaRepository {
    Empresa save(Empresa empresa);
    boolean existsByDocumento(String documento);
}
