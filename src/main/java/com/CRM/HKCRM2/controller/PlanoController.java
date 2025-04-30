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
import com.CRM.HKCRM2.Service.PlanoService;
import com.CRM.HKCRM2.dtos.PlanoDtos;
import com.CRM.HKCRM2.model.Plano;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/planos")                            // Define o caminho base para os endpoints deste controlador
public class PlanoController {

    @Autowired                                       // Injeção de dependência do serviço de plano
    private PlanoService service;                    // Injeção de dependência do serviço de plano


    @GetMapping("/listar")                           // Define o endpoint para listar todos os planos
    public ResponseEntity<List<Plano>> getAll() {    // Método para listar todos os planos
        return ResponseEntity.ok(service.listAll()); // Retorna a lista de planos com status 200 OK
}

@GetMapping("/{id}")
public ResponseEntity<Plano> getById(@PathVariable Integer id) {
  Plano plano = service.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado"));
  return ResponseEntity.ok(plano);
}

    @PostMapping("/criar") // Define o endpoint para criar um novo plano
    public ResponseEntity<Plano> create(@RequestBody @Valid PlanoDtos dto) {
        Plano created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);


    }
    @PutMapping("/alterar/{id}")
    public ResponseEntity<Plano> update(@PathVariable Integer id, @RequestBody @Valid PlanoDtos dto) {
        // Se não encontrar, lança exceção que vira 404 automaticamente
        Plano atualizado = service.update(id, dto)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado")
            );
    
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.delete(id)) {
            return ResponseEntity.ok("Plano deletado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado");
    }
}    
