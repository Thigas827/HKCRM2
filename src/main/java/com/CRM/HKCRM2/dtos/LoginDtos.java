package com.CRM.HKCRM2.dtos;

public record LoginDtos( String email, String senha) {
    // DTO para login, contendo apenas os campos necessários
    // Não expõe a senha em outros lugares
    // O Spring Security irá lidar com a autenticação

}
