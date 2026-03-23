package com.senai.karina.sistema_de_clinica.repository;

import com.senai.karina.sistema_de_clinica.model.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

// sem métodos extras por enquanto, o JpaRepository já resolve
public interface FichaMedicaRepository extends JpaRepository<FichaMedica, Long> {
}