package com.CRM.HKCRM2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import com.CRM.HKCRM2.Service.PlanoService;
import com.CRM.HKCRM2.model.Plano;

@RestController
@RequestMapping("/plano")                            // Define o caminho base para os endpoints deste controlador
public class PlanoController {

    @Autowired                                       // Injeção de dependência do serviço de plano
    private PlanoService service;                    // Injeção de dependência do serviço de plano


    @GetMapping
    public ResponseEntity<List<Plano>> getAll() {    // Método para listar todos os planos
        return ResponseEntity.ok(service.listAll()); // Retorna a lista de planos com status 200 OK
}

    @GetMapping("/{id}")                             // Método para obter um plano específico pelo ID
    public ResponseEntity<?> getById(@PathVariable Integer id) { // Recebe o ID do plano como parâmetro
        return service.findById(id).map(plano -> ResponseEntity.ok(plano)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado"));

    }
}
