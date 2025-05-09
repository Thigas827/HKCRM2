package com.CRM.HKCRM2.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.CRM.HKCRM2.Service.DoceService;
import com.CRM.HKCRM2.Service.PlanoService;
import com.CRM.HKCRM2.dtos.DoceDtos;
import com.CRM.HKCRM2.model.Doce;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/doces")                            // Define o caminho base para os endpoints deste controlador
public class DoceController {

    @Autowired                                       // Injeção de dependência do serviço de doce
    private DoceService service;                    // Injeção de dependência do serviço de doce


    @GetMapping
    public ResponseEntity<List<Doce>> getAll() {    // Método para listar todos os planos
        return ResponseEntity.ok(service.listAll()); // Retorna a lista de Doces com status 200 OK
}

@GetMapping("/{id}")
public ResponseEntity<Doce> getById(@PathVariable Integer id) {
  Doce doce = service.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doce não encontrado"));
  return ResponseEntity.ok(doce);
}

    @PostMapping("/criar") // Define o endpoint para criar um novo doce

    public ResponseEntity<Doce> create(@RequestBody @Valid DoceDtos dto) {
        Doce created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);


    }
    @PutMapping("/alterar/{id}")
    public ResponseEntity<Doce> update(@PathVariable Integer id, @RequestBody @Valid DoceDtos dto) {
        // Se não encontrar, lança exceção que vira 404 automaticamente
        Doce atualizado = service.update(id, dto)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Doce não encontrado")
            );
    
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.delete(id)) {
            return ResponseEntity.ok("Doce deletado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doce não encontrado");
    }
}    

