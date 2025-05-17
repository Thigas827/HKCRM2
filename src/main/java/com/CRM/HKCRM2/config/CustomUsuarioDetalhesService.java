package com.CRM.HKCRM2.config;
import java.util.stream.Collectors;
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.repositories.UIUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

@Service
// Implementa a interface UserDetailsService para carregar detalhes do usuário
public class CustomUsuarioDetalhesService implements UserDetailsService {

    @Autowired
    private UIUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário pelo email no repositório
        UsuarioMod usuario = usuarioRepository.findByEmail(email);
        // Se o usuário não for encontrado, lança uma exceção
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        // Converte as permissões do usuário em uma lista de autoridades
        var authorities = usuario.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getNome()))
                .collect(Collectors.toList());
        // Retorna um objeto UserDetails com as informações do usuário
        
        // Devolve um objeto UserDetails com as informações do usuário (email, senha e permissões)
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                authorities
        );
    }
}
