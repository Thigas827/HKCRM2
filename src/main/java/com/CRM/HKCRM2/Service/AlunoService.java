package com.CRM.HKCRM2.Service;

import com.CRM.HKCRM2.dtos.AlunoDto;
import com.CRM.HKCRM2.model.Aluno;
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.repositories.AlunoRepository;
import com.CRM.HKCRM2.repositories.UIUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private UIUsuarioRepository usuarioRepository;

    public AlunoDto criarAluno(AlunoDto dto) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome);
        aluno.setEmail(dto.email);
        aluno.setEndereco(dto.endereco);
        aluno.setTelefone(dto.telefone);
        aluno.setDataNascimento(dto.dataNascimento);
        aluno.setPeso(dto.peso);
        aluno.setAltura(dto.altura);
        //aluno.setSenha(dto.senha != null ? dto.senha : "123456");
        // Busca usuário pelo email
        UsuarioMod usuario = usuarioRepository.findByEmail(dto.email).orElse(null);
        if (usuario == null) {
            // Criação simplificada de usuário (pode ser melhorada)
            usuario = new UsuarioMod();
            usuario.setNome(dto.nome);
            usuario.setEmail(dto.email);
            //usuario.setSenha(dto.senha != null ? dto.senha : "123456");
            usuario.setEndereco(dto.endereco);
            usuario.setTelefone(dto.telefone);
            usuario = usuarioRepository.save(usuario);
        }
        aluno.setUsuario(usuario);
        aluno = alunoRepository.save(aluno);
        dto.id = aluno.getId();
        dto.usuarioId = usuario.getId();
        return dto;
    }

    public List<AlunoDto> listarAlunos() {
        return alunoRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<AlunoDto> buscarPorId(Long id) {
        return alunoRepository.findById(id).map(this::toDto);
    }

    private AlunoDto toDto(Aluno aluno) {
        AlunoDto dto = new AlunoDto();
        dto.id = aluno.getId();
        dto.nome = aluno.getNome();
        dto.email = aluno.getEmail();
        dto.endereco = aluno.getEndereco();
        dto.telefone = aluno.getTelefone();
        dto.dataNascimento = aluno.getDataNascimento();
        dto.peso = aluno.getPeso();
        dto.altura = aluno.getAltura();
        dto.usuarioId = aluno.getUsuario() != null ? aluno.getUsuario().getId() : null;
        return dto;
    }
}
