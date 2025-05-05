package com.CRM.HKCRM2.Erros;

public class UsuarioJaExiste extends RuntimeException {
    private String mensagem;
    public UsuarioJaExiste() {
      this.mensagem = "Usuario já existe";
    }
  
    public String getMensagem() {
        return mensagem;
    }
  
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
  }