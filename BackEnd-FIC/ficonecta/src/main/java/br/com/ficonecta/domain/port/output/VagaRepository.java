package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface VagaRepository {
    Vaga save(Vaga vaga);

    long countByStatus(StatusVaga status);

    long countByEmpresaIdAndStatus(UUID empresaId, StatusVaga status);

    Page<Vaga> findByEmpresaId(UUID empresaId, Pageable pageable);

    Page<Vaga> findByEmpresaIdAndStatus(UUID empresaId, StatusVaga status, Pageable pageable);

    Page<Vaga> findByStatusAndDataVagaGreaterThanEqual(StatusVaga status, LocalDate data, Pageable pageable);

    long countByStatusAndDataVagaGreaterThanEqual(StatusVaga status, LocalDate data);

    Optional<Vaga> findByIdAndEmpresaId(UUID id, UUID empresaId);
}
