package com.senai.karina.sistema_de_clinica.model;

import com.senai.karina.sistema_de_clinica.enums.Sexo;
import com.senai.karina.sistema_de_clinica.enums.StatusPaciente;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// dados cadastrais do paciente
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 120)
    private String nome;

    // cpf único — validado no service
    @NotBlank
    @Size(min = 11, max = 14)
    @Column(unique = true)
    private String cpf;

    // email único — validado no service
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Size(max = 20)
    private String telefone;

    // data de nascimento tem que ser no passado
    @NotNull
    @Past
    private LocalDate dataNascimento;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    // começa sempre como ATIVO — definido automaticamente no prePersist, não precisa vir no JSON
    @Enumerated(EnumType.STRING)
    private StatusPaciente statusPaciente;

    // preenchida automaticamente quando o paciente é criado
    private LocalDateTime dataCadastro;

    // fase 1: unidirecional com EAGER (padrão do JPA)
    // a coluna ficha_id fica na tabela pacientes
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ficha_id", unique = true)
    private FichaMedica fichaMedica;

    // fase 2: trocar o bloco acima por:
    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // @JoinColumn(name = "ficha_id", unique = true)
    // @JsonManagedReference
    // private FichaMedica fichaMedica;

    // roda antes de salvar: define dataCadastro e statusPaciente inicial
    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        this.statusPaciente = StatusPaciente.ATIVO;
    }

    public Paciente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public StatusPaciente getStatusPaciente() { return statusPaciente; }
    public void setStatusPaciente(StatusPaciente statusPaciente) { this.statusPaciente = statusPaciente; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public FichaMedica getFichaMedica() { return fichaMedica; }
    public void setFichaMedica(FichaMedica fichaMedica) { this.fichaMedica = fichaMedica; }
}