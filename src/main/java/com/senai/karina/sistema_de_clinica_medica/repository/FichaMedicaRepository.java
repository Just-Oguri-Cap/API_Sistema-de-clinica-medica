package com.senai.karina.sistema_de_clinica_medica.repository;

import com.senai.karina.sistema_de_clinica_medica.model.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

// sem métodos extras por enquanto, o JpaRepository já resolve
public interface FichaMedicaRepository extends JpaRepository<FichaMedica, Long> {
}