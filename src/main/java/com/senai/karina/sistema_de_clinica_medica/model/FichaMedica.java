package com.senai.karina.sistema_de_clinica_medica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.senai.karina.sistema_de_clinica_medica.enums.TipoSanguineo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// dados clínicos do paciente — carregados só quando precisar
@Entity
@Table(name = "fichas_medicas")
public class FichaMedica {

    // fase 1 e 2: id gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineo;

    // campos opcionais, podem ser nulos
    @Size(max = 500)
    private String alergias;

    @Size(max = 500)
    private String medicamentosUso;

    @Size(max = 1000)
    private String historicoDoencas;

    @Size(max = 1000)
    private String observacoesClinicas;

    // preenchida automaticamente a cada PUT /ficha
    private LocalDateTime dataAtualizacao;

    // fase 2: lado inverso do relacionamento
    // @JsonBackReference evita o loop infinito no JSON (ficha -> paciente -> ficha -> ...)
    @OneToOne(mappedBy = "fichaMedica")
    @JsonBackReference
    private Paciente paciente;

    public FichaMedica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoSanguineo getTipoSanguineo() { return tipoSanguineo; }
    public void setTipoSanguineo(TipoSanguineo tipoSanguineo) { this.tipoSanguineo = tipoSanguineo; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getMedicamentosUso() { return medicamentosUso; }
    public void setMedicamentosUso(String medicamentosUso) { this.medicamentosUso = medicamentosUso; }

    public String getHistoricoDoencas() { return historicoDoencas; }
    public void setHistoricoDoencas(String historicoDoencas) { this.historicoDoencas = historicoDoencas; }

    public String getObservacoesClinicas() { return observacoesClinicas; }
    public void setObservacoesClinicas(String observacoesClinicas) { this.observacoesClinicas = observacoesClinicas; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
}