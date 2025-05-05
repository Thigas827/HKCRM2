package com.CRM.HKCRM2.Service;

import java.util.List;

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

    @Autowired
    private UIUsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioMod criarUsuario(UsuarioMod usuario) {
        // Verifica se o usuário já existe
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new UsuarioJaExiste();
        }

        // 2) Hash da senha — AQUI, use a variável 'usuario', não a classe
        String hash = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(hash);

        // 3) Garante que a role CLIENTE exista (ou cria)
        Role clienteRole = roleRepository.findByNome("ROLE_CLIENTE");
        if (clienteRole == null) {
            clienteRole = new Role();
            clienteRole.setName("ROLE_CLIENTE");
            clienteRole = roleRepository.save(clienteRole);
        }

        // 4) Associa ao usuário e salva
        usuario.getRoles().add(clienteRole);
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

