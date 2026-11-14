package br.com.ficonecta.adapter.output.persistence.repository;

import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface VagaJpaRepository extends VagaRepository, JpaRepository<Vaga, UUID> {

    @Override
    @EntityGraph(attributePaths = "empresa")
    Page<Vaga> findByStatusAndDataVagaGreaterThanEqual(StatusVaga status, LocalDate data, Pageable pageable);


    @Override
    @Modifying
    @Query("update Vaga v set v.status = :novoStatus, v.updatedAt = :agora "
            + "where v.status = :statusAtual and v.dataVaga < :data")
    int alterarStatusDeVagasAnterioresA(@Param("statusAtual") StatusVaga statusAtual,
                                        @Param("novoStatus") StatusVaga novoStatus,
                                        @Param("data") LocalDate data,
                                        @Param("agora") LocalDateTime agora);
}
