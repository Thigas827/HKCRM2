package com.CRM.HKCRM2.dtos;

import java.util.List;
import java.util.UUID;

public record CompraDtos(UUID clienteId, List<ItemDtos> itens) {
    // Construtor, getters e outros métodos podem ser adicionados aqui, se necessário
    // O UUID é usado para representar um identificador único universal
    // A lista de ItemDtos representa os itens da compra

}
