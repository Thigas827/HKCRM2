package com.CRM.HKCRM2.controller;
import com.CRM.HKCRM2.repositories.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import com.CRM.HKCRM2.dtos.CompraDtos;
import com.CRM.HKCRM2.dtos.CompraDetalhesDto;
import com.CRM.HKCRM2.model.Compra;
import com.CRM.HKCRM2.Service.CompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping()
    public ResponseEntity<Compra> criarCompra(@RequestBody @Valid CompraDtos dto) {
        Compra compra = compraService.registrarCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compra);
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listarCompras(@RequestParam UUID clienteId) {
        List<Compra> lista = compraService.listarPorCliente(clienteId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/detalhes/{id}")
    public ResponseEntity<CompraDetalhesDto> buscarDetalhesCompra(@PathVariable Integer id) {
        CompraDetalhesDto detalhes = compraService.buscarDetalhesCompra(id);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/detalhes")
    public ResponseEntity<List<CompraDetalhesDto>> listarDetalhesCompras(@RequestParam UUID clienteId) {
        List<CompraDetalhesDto> detalhes = compraService.listarDetalhesComprasPorCliente(clienteId);
        if (detalhes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(detalhes);
    }
}