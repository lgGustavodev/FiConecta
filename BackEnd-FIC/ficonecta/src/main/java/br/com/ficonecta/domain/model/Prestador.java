package br.com.ficonecta.domain.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prestadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 14)
    private String cpf;

    @Column(length = 255)
    private String foto;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Builder.Default
    @Column(nullable = false)
    private boolean disponivel = true;

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
