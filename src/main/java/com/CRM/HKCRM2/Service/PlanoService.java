package com.CRM.HKCRM2.Service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.CRM.HKCRM2.repositories.PlanoRepository;
import com.CRM.HKCRM2.repositories.UIUsuarioRepository;
import com.CRM.HKCRM2.model.Plano;
import com.CRM.HKCRM2.dtos.PlanoDtos;
import com.CRM.HKCRM2.dtos.CompraDtos;
import com.CRM.HKCRM2.dtos.ItemDtos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlanoService {

    @Autowired // Injeção de dependência do repositório
    private PlanoRepository repository;

    @Autowired
    private CompraService compraService;

    @Autowired
    private UIUsuarioRepository usuarioRepository;

    public List<Plano> listAll() { // Método para listar todos os planos
        return repository.findAll();

    }

    public Optional<Plano> findById(Integer id) { // Método para encontrar um plano pelo ID
        return repository.findById(id);

    }

    public Plano create(PlanoDtos dtos) { // Método para criar um novo plano
        Plano plano = new Plano();
        BeanUtils.copyProperties(dtos, plano);

        // Se nãoi veio dataVencimento no DTO, calcula 30 dias após dataFim

        if (plano.getDataVencimento() == null) { // Verifica se a data de vencimento é nula
            plano.setDataVencimento(plano.getDataFim().plusDays(30));
        }

        return repository.save(plano); // Salva o plano no repositório
    }

    public Optional<Plano> update(Integer id, PlanoDtos dtos) { // Método para atualizar um plano existente
        return repository.findById(id).map(existing -> {
            BeanUtils.copyProperties(dtos, existing);
            return repository.save(existing); // Atualiza o plano no repositório
        });

    }

    public boolean delete(Integer id) { // Método para deletar um plano pelo ID
        return repository.findById(id).map(existing -> {
            repository.delete(existing); // Deleta o plano do repositório
            return true;
        }).orElse(false); // Retorna false se o plano não for encontrado

    }

    @Transactional
    public void contratar(Integer planoId, UUID clienteId) {
        // Busca o plano
        Plano plano = repository.findById(planoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado"));

        // Verifica se o usuário existe
        usuarioRepository.findById(clienteId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Cria DTO de compra
        CompraDtos compraDtos = new CompraDtos(
            clienteId,
            List.of(new ItemDtos(planoId, 1))
        );

        // Registra a compra
        compraService.registrarCompra(compraDtos);
    }
}
