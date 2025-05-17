package com.CRM.HKCRM2.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CRM.HKCRM2.Erros.UsuarioJaExiste;
import com.CRM.HKCRM2.model.Role;
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.repositories.RoleRepository;
import com.CRM.HKCRM2.repositories.UIUsuarioRepository;

/**
 * Serviço responsável por gerenciar as operações relacionadas aos usuários no sistema.
 * 
 * Funcionalidades principais:
 * - Criar um novo usuário, garantindo que o e-mail seja único, a senha seja criptografada
 *   e que o usuário receba a role padrão "ROLE_CLIENTE".
 * - Autenticar um usuário verificando o e-mail e comparando a senha fornecida com o hash armazenado.
 * - Listar todos os usuários cadastrados.
 * - Buscar um usuário pelo e-mail.
 * - Deletar um usuário pelo e-mail.
 * 
 * Este serviço utiliza repositórios para persistência de dados e um codificador de senhas
 * para garantir a segurança das credenciais.
 */
@Service
public class UsuarioService {

    /**
     * Repositório para operações com usuários no banco de dados
     */
    @Autowired
    private UIUsuarioRepository usuarioRepository;

    /**
     * Repositório para operações com roles/papéis no banco de dados
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Serviço de criptografia de senhas
     * Usado para garantir que as senhas sejam armazenadas de forma segura
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioMod criarUsuario(UsuarioMod usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new UsuarioJaExiste();
        }
        String hash = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(hash);

        Set<Role> rolesSalvas = new HashSet<>();
        for (Role role : usuario.getRoles()) {
            Role roleSalva = roleRepository.findByNome(role.getNome());
            if (roleSalva == null) {
                roleSalva = new Role();
                roleSalva.setName(role.getNome());
                roleSalva = roleRepository.save(roleSalva);
            }
            rolesSalvas.add(roleSalva);
        }
        usuario.setRoles(rolesSalvas);

        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String email, String senha) {
        UsuarioMod usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return false;
        }
        // compara raw vs hash
        return passwordEncoder.matches(senha, usuario.getSenha());
    }

    // Métodos auxiliares — úteis para outros endpoints:
    
    /** Lista todos os usuários. */
    public List<UsuarioMod> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /** Busca um usuário pelo e-mail. */
    public UsuarioMod buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /** Deleta usuário pelo e-mail. */
    @Transactional
    public void deletarPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}

