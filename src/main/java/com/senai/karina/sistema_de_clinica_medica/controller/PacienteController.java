package com.senai.karina.sistema_de_clinica_medica.controller;

import com.senai.karina.sistema_de_clinica_medica.enums.StatusPaciente;
import com.senai.karina.sistema_de_clinica_medica.enums.TipoSanguineo;
import com.senai.karina.sistema_de_clinica_medica.model.FichaMedica;
import com.senai.karina.sistema_de_clinica_medica.model.Paciente;
import com.senai.karina.sistema_de_clinica_medica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// recebe as requisições e passa pro service resolver
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // POST /api/pacientes
    @PostMapping
    public ResponseEntity<Paciente> criar(@Valid @RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(paciente));
    }

    // GET /api/pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    // GET /api/pacientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    // GET /api/pacientes/{id}/completo — traz paciente + ficha em um único SELECT
    @GetMapping("/{id}/completo")
    public ResponseEntity<Paciente> buscarComFicha(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarComFicha(id));
    }

    // GET /api/pacientes/cpf/{cpf}
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.buscarPorCpf(cpf));
    }

    // PUT /api/pacientes/{id} — cpf, status e ficha não são alterados aqui
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizar(@PathVariable Long id,
                                              @Valid @RequestBody Paciente dados) {
        return ResponseEntity.ok(pacienteService.atualizar(id, dados));
    }

    // DELETE /api/pacientes/{id} — soft delete, só muda pra INATIVO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        pacienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/pacientes/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Paciente>> filtrarPorStatus(@PathVariable StatusPaciente status) {
        return ResponseEntity.ok(pacienteService.filtrarPorStatus(status));
    }

    // GET /api/pacientes/buscar?termo=silva
    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscar(@RequestParam String termo) {
        return ResponseEntity.ok(pacienteService.buscarPorTermo(termo));
    }

    // GET /api/pacientes/com-ficha
    @GetMapping("/com-ficha")
    public ResponseEntity<List<Paciente>> comFicha() {
        return ResponseEntity.ok(pacienteService.comFicha());
    }

    // GET /api/pacientes/sem-ficha
    @GetMapping("/sem-ficha")
    public ResponseEntity<List<Paciente>> semFicha() {
        return ResponseEntity.ok(pacienteService.semFicha());
    }

    // GET /api/pacientes/tipo-sanguineo/{tipo}
    @GetMapping("/tipo-sanguineo/{tipo}")
    public ResponseEntity<List<Paciente>> filtrarPorTipoSanguineo(@PathVariable TipoSanguineo tipo) {
        return ResponseEntity.ok(pacienteService.filtrarPorTipoSanguineo(tipo));
    }

    // PATCH /api/pacientes/{id}/ativar
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Paciente> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.ativar(id));
    }

    // PATCH /api/pacientes/{id}/inativar
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Paciente> inativarPatch(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.inativarPatch(id));
    }

    // PATCH /api/pacientes/{id}/suspender
    @PatchMapping("/{id}/suspender")
    public ResponseEntity<Paciente> suspender(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.suspender(id));
    }

    // POST /api/pacientes/{id}/ficha
    @PostMapping("/{id}/ficha")
    public ResponseEntity<Paciente> adicionarFicha(@PathVariable Long id,
                                                   @Valid @RequestBody FichaMedica ficha) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.adicionarFicha(id, ficha));
    }

    // GET /api/pacientes/{id}/ficha
    @GetMapping("/{id}/ficha")
    public ResponseEntity<FichaMedica> buscarFicha(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarFicha(id));
    }

    // PUT /api/pacientes/{id}/ficha — atualiza dados clínicos
    @PutMapping("/{id}/ficha")
    public ResponseEntity<FichaMedica> atualizarFicha(@PathVariable Long id,
                                                      @Valid @RequestBody FichaMedica dados) {
        return ResponseEntity.ok(pacienteService.atualizarFicha(id, dados));
    }

    // DELETE /api/pacientes/{id}/ficha — remove a ficha, paciente continua
    @DeleteMapping("/{id}/ficha")
    public ResponseEntity<Void> removerFicha(@PathVariable Long id) {
        pacienteService.removerFicha(id);
        return ResponseEntity.noContent().build();
    }
}