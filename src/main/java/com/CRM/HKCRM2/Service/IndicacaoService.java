package com.CRM.HKCRM2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.CRM.HKCRM2.repositories.UIUsuarioRepository;
import com.CRM.HKCRM2.model.UsuarioMod;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class IndicacaoService {

    @Autowired
    private UIUsuarioRepository usuarioRepository;

    @Transactional
    public void processarIndicacao(UUID indicadorId, String emailIndicado) {
        // Verifica se o usuário indicador existe
        UsuarioMod indicador = usuarioRepository.findById(indicadorId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário indicador não encontrado"));

        // Verifica se o email indicado já está cadastrado
        if (usuarioRepository.findByEmail(emailIndicado).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado no sistema");
        }

        // Aqui você pode adicionar a lógica para enviar o email de convite
        // e registrar a indicação em uma tabela de indicações

        // Adiciona 15 dias extras ao plano do indicador
        // Note: Isso é apenas um exemplo, você precisa ajustar conforme sua regra de negócio
        if (indicador.getDataFimPlano() != null) {
            indicador.setDataFimPlano(indicador.getDataFimPlano().plusDays(15));
            usuarioRepository.save(indicador);
        }
    }
}
