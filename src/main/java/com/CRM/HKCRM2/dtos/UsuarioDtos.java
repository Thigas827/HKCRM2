package com.CRM.HKCRM2.dtos;

import java.util.UUID;

public record UsuarioDtos( 
    UUID id,
    String nome,
    String email,
    String telefone,
    String endereco
) {

}
