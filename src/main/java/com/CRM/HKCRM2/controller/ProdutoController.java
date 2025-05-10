package com.CRM.HKCRM2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CRM.HKCRM2.Service.ProdutoService;
import com.CRM.HKCRM2.dtos.ProdutoDto;
import com.CRM.HKCRM2.model.Produto;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST para gerenciamento de produtos.
 * 
 * Endpoints disponíveis:
 * - GET /produtos: Lista todos os produtos cadastrados
 * - POST /produtos: Cadastra um novo produto
 * 
 * Esta classe faz parte da API REST e é responsável por:
 * - Receber requisições HTTP relacionadas a produtos
 * - Validar dados de entrada
 * - Encaminhar as operações para o serviço apropriado
 * - Retornar respostas formatadas adequadamente
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = service.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid ProdutoDto dto) {
        Produto produto = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }
}
