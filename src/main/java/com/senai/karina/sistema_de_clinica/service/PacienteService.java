package com.senai.karina.sistema_de_clinica.service;

import com.senai.karina.sistema_de_clinica.enums.StatusPaciente;
import com.senai.karina.sistema_de_clinica.enums.TipoSanguineo;
import com.senai.karina.sistema_de_clinica.exception.RecursoNaoEncontradoException;
import com.senai.karina.sistema_de_clinica.exception.RegraDeNegocioException;
import com.senai.karina.sistema_de_clinica.model.FichaMedica;
import com.senai.karina.sistema_de_clinica.model.Paciente;
import com.senai.karina.sistema_de_clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // cria paciente novo — cpf e email não podem repetir
    @Transactional
    public Paciente criar(Paciente paciente) {
        if (pacienteRepository.existsByCpf(paciente.getCpf())) {
            throw new RegraDeNegocioException("CPF já cadastrado: " + paciente.getCpf());
        }
        if (pacienteRepository.existsByEmail(paciente.getEmail())) {
            throw new RegraDeNegocioException("E-mail já cadastrado: " + paciente.getEmail());
        }
        paciente.setFichaMedica(null);
        return pacienteRepository.save(paciente);
    }

    // lista todos sem carregar a ficha (lazy)
    @Transactional(readOnly = true)
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    // busca pelo id, lança 404 se não achar
    @Transactional(readOnly = true)
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado: " + id));
    }

    // busca paciente já com a ficha carregada num único SELECT
    @Transactional(readOnly = true)
    public Paciente buscarComFicha(Long id) {
        return pacienteRepository.findByIdComFicha(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado: " + id));
    }

    // busca pelo cpf
    @Transactional(readOnly = true)
    public Paciente buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com CPF: " + cpf));
    }

    // atualiza dados cadastrais
    // cpf, dataCadastro, statusPaciente e fichaMedica não podem ser alterados aqui
    @Transactional
    public Paciente atualizar(Long id, Paciente dados) {
        Paciente paciente = buscarPorId(id);
        paciente.setNome(dados.getNome());
        paciente.setEmail(dados.getEmail());
        paciente.setTelefone(dados.getTelefone());
        paciente.setDataNascimento(dados.getDataNascimento());
        paciente.setSexo(dados.getSexo());
        return pacienteRepository.save(paciente);
    }

    // soft delete — não apaga do banco, só muda pra INATIVO
    @Transactional
    public void inativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setStatusPaciente(StatusPaciente.INATIVO);
        pacienteRepository.save(paciente);
    }

    // filtra por status
    @Transactional(readOnly = true)
    public List<Paciente> filtrarPorStatus(StatusPaciente status) {
        return pacienteRepository.findByStatusPaciente(status);
    }

    // busca por nome ou email
    @Transactional(readOnly = true)
    public List<Paciente> buscarPorTermo(String termo) {
        return pacienteRepository.buscarPorTermo(termo);
    }

    // só os que já têm ficha vinculada
    @Transactional(readOnly = true)
    public List<Paciente> comFicha() {
        return pacienteRepository.findComFicha();
    }

    // só os que ainda não têm ficha
    @Transactional(readOnly = true)
    public List<Paciente> semFicha() {
        return pacienteRepository.findSemFicha();
    }

    // filtra pelo tipo sanguíneo da ficha
    @Transactional(readOnly = true)
    public List<Paciente> filtrarPorTipoSanguineo(TipoSanguineo tipo) {
        return pacienteRepository.findByTipoSanguineo(tipo);
    }

    // muda pra ATIVO (funciona de INATIVO ou SUSPENSO)
    @Transactional
    public Paciente ativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setStatusPaciente(StatusPaciente.ATIVO);
        return pacienteRepository.save(paciente);
    }

    // muda pra INATIVO via patch
    @Transactional
    public Paciente inativarPatch(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setStatusPaciente(StatusPaciente.INATIVO);
        return pacienteRepository.save(paciente);
    }

    // muda pra SUSPENSO — não pode se já estiver INATIVO
    @Transactional
    public Paciente suspender(Long id) {
        Paciente paciente = buscarPorId(id);
        if (paciente.getStatusPaciente() == StatusPaciente.INATIVO) {
            throw new RegraDeNegocioException("Não é possível suspender um paciente INATIVO.");
        }
        paciente.setStatusPaciente(StatusPaciente.SUSPENSO);
        return pacienteRepository.save(paciente);
    }

    // cria e vincula a ficha ao paciente
    // só funciona se o paciente estiver ATIVO e ainda não tiver ficha
    @Transactional
    public Paciente adicionarFicha(Long pacienteId, FichaMedica ficha) {
        Paciente paciente = buscarPorId(pacienteId);

        if (paciente.getStatusPaciente() != StatusPaciente.ATIVO) {
            throw new RegraDeNegocioException(
                    "Só é possível criar ficha para paciente ATIVO. Status atual: " + paciente.getStatusPaciente());
        }
        if (paciente.getFichaMedica() != null) {
            throw new RegraDeNegocioException("Este paciente já tem ficha médica.");
        }

        // sincroniza os dois lados do relacionamento (necessário na fase 2)
        ficha.setPaciente(paciente);
        paciente.setFichaMedica(ficha);
        return pacienteRepository.save(paciente);
    }

    // retorna a ficha do paciente, 404 se não tiver
    @Transactional(readOnly = true)
    public FichaMedica buscarFicha(Long pacienteId) {
        Paciente paciente = buscarComFicha(pacienteId);
        if (paciente.getFichaMedica() == null) {
            throw new RecursoNaoEncontradoException("Este paciente não tem ficha médica.");
        }
        return paciente.getFichaMedica();
    }

    // atualiza os dados clínicos e registra quando foi atualizado
    @Transactional
    public FichaMedica atualizarFicha(Long pacienteId, FichaMedica dados) {
        Paciente paciente = buscarComFicha(pacienteId);

        if (paciente.getStatusPaciente() == StatusPaciente.INATIVO) {
            throw new RegraDeNegocioException("Não é possível atualizar ficha de paciente INATIVO.");
        }

        FichaMedica atual = paciente.getFichaMedica();
        if (atual == null) {
            throw new RecursoNaoEncontradoException("Este paciente não tem ficha médica.");
        }

        atual.setTipoSanguineo(dados.getTipoSanguineo());
        atual.setAlergias(dados.getAlergias());
        atual.setMedicamentosUso(dados.getMedicamentosUso());
        atual.setHistoricoDoencas(dados.getHistoricoDoencas());
        atual.setObservacoesClinicas(dados.getObservacoesClinicas());
        atual.setDataAtualizacao(LocalDateTime.now());

        return pacienteRepository.save(paciente).getFichaMedica();
    }

    // remove a ficha — o paciente continua, orphanRemoval deleta do banco sozinho
    @Transactional
    public void removerFicha(Long pacienteId) {
        Paciente paciente = buscarComFicha(pacienteId);
        if (paciente.getFichaMedica() == null) {
            throw new RecursoNaoEncontradoException("Este paciente não tem ficha médica.");
        }
        paciente.setFichaMedica(null);
        pacienteRepository.save(paciente);
    }
}