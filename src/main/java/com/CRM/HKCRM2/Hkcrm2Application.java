package com.CRM.HKCRM2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal do sistema HKCRM2 - Sistema de Gestão de Vendas
 * 
 * Esta é a classe que inicia toda a aplicação Spring Boot.
 * O sistema é um MVP (Minimum Viable Product) que fornece funcionalidades básicas para:
 * - Gestão de produtos (cadastro, listagem e busca)
 * - Gestão de clientes (cadastro e manutenção)
 * - Registro e acompanhamento de vendas
 * - Interface web responsiva
 */
@SpringBootApplication
public class Hkcrm2Application {

    /**
     * Método principal que inicia a aplicação Spring Boot
     * 
     * @param args argumentos de linha de comando (não utilizados neste MVP)
     */
    public static void main(String[] args) {
        SpringApplication.run(Hkcrm2Application.class, args);
    }

}
