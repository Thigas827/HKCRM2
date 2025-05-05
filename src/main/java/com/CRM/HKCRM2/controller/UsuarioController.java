package com.CRM.HKCRM2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.CRM.HKCRM2.Service.UsuarioService;
import com.CRM.HKCRM2.dtos.LoginDtos;
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.Erros.UsuarioJaExiste;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/criar")
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioMod usuario) {
        try {
            UsuarioMod usuarioCriado = usuarioService.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (UsuarioJaExiste e) {
            // 400 Bad Request se o e-mail já existir
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDtos dto) {
        boolean ok = usuarioService.autenticar(dto.email(), dto.senha());
        if (ok) {
            return ResponseEntity.ok("Login bem-sucedido");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("E-mail ou senha incorretos");
    }
    
}