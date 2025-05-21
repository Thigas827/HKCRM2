package com.CRM.HKCRM2.controller;

import com.CRM.HKCRM2.dtos.AlunoDto;
import com.CRM.HKCRM2.Service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @PostMapping("/criar")
    public ResponseEntity<AlunoDto> criarAluno(@RequestBody AlunoDto dto) {
        return ResponseEntity.ok(alunoService.criarAluno(dto));
    }

    @GetMapping("/todos")
    public ResponseEntity<List<AlunoDto>> listarAlunos() {
        return ResponseEntity.ok(alunoService.listarAlunos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDto> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/usuario")
    public ResponseEntity<java.util.UUID> buscarUsuarioIdPorAluno(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
            .map(dto -> ResponseEntity.ok(dto.usuarioId))
            .orElse(ResponseEntity.notFound().build());
    }
}
