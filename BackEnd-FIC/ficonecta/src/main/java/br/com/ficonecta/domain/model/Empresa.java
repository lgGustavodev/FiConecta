package br.com.ficonecta.domain.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", length = 10)
    private TipoDocumento tipoDocumento;

    @Column(length = 20)
    private String documento;

    @Column(length = 255)
    private String foto;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
