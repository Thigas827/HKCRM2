package com.CRM.HKCRM2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.CRM.HKCRM2.dtos.CompraDtos;
import com.CRM.HKCRM2.model.Compra;
import com.CRM.HKCRM2.Service.CompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    /**
     * Registra uma nova compra.
     * POST /compras
     */
    @PostMapping
    public ResponseEntity<Compra> criarCompra(@RequestBody @Valid CompraDtos dto) {
        Compra compra = compraService.registrarCompra(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compra);
    }
}