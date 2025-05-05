package com.CRM.HKCRM2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.CRM.HKCRM2.Erros.UsuarioJaExiste;
import com.CRM.HKCRM2.model.Role;
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.repositories.RoleRepository;
import com.CRM.HKCRM2.repositories.UIUsuarioRepository;

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

}
