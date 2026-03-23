package com.senai.karina.sistema_de_clinica_medica.repository;

import com.senai.karina.sistema_de_clinica_medica.enums.StatusPaciente;
import com.senai.karina.sistema_de_clinica_medica.enums.TipoSanguineo;
import com.senai.karina.sistema_de_clinica_medica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // busca paciente junto com a ficha num único SELECT (evita query extra no LAZY)
    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.fichaMedica WHERE p.id = :id")
    Optional<Paciente> findByIdComFicha(@Param("id") Long id);

    // busca pelo CPF
    Optional<Paciente> findByCpf(String cpf);

    // busca por nome ou email usando LIKE, ignora maiúsculas/minúsculas
    @Query("SELECT p FROM Paciente p WHERE " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :t, '%'))")
    List<Paciente> buscarPorTermo(@Param("t") String termo);

    // filtra por status (ATIVO, INATIVO, SUSPENSO)
    List<Paciente> findByStatusPaciente(StatusPaciente status);

    // filtra pelo tipo sanguíneo da ficha
    @Query("SELECT p FROM Paciente p JOIN p.fichaMedica f WHERE f.tipoSanguineo = :tipo")
    List<Paciente> findByTipoSanguineo(@Param("tipo") TipoSanguineo tipo);

    // só os que já têm ficha
    @Query("SELECT p FROM Paciente p WHERE p.fichaMedica IS NOT NULL")
    List<Paciente> findComFicha();

    // só os que ainda não têm ficha
    @Query("SELECT p FROM Paciente p WHERE p.fichaMedica IS NULL")
    List<Paciente> findSemFicha();

    // verifica se já existe esse email (usado pra evitar duplicata)
    boolean existsByEmail(String email);

    // verifica se já existe esse cpf (usado pra evitar duplicata)
    boolean existsByCpf(String cpf);
}