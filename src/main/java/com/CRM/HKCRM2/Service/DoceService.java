package com.CRM.HKCRM2.Service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CRM.HKCRM2.repositories.DoceRepository;
import com.CRM.HKCRM2.model.Doce;
import com.CRM.HKCRM2.dtos.DoceDtos;

import java.util.List;
import java.util.Optional;

@Service
public class DoceService {

    @Autowired // Injeção de dependência do repositório
    private DoceRepository repository;

    public List<Doce> listAll() { // Método para listar todos os Doces
        return repository.findAll();

    }

    public Optional<Doce> findById(Integer id) { // Método para encontrar um doce pelo ID
        return repository.findById(id);

    }

    public Doce create(DoceDtos dtos) { // Método para criar um novo plano
        Doce doce = new Doce();
        BeanUtils.copyProperties(dtos, doce);
        return repository.save(doce); // Salva o plano no repositório
    }

    public Optional<Doce> update(Integer id, DoceDtos dtos) { // Método para atualizar um plano existente
        return repository.findById(id).map(existing -> {
            BeanUtils.copyProperties(dtos, existing);
            return repository.save(existing); // Atualiza o plano no repositório
        });

    }

    public boolean delete(Integer id) { // Método para deletar um doce pelo ID
        return repository.findById(id).map(existing -> {
            repository.delete(existing); // Deleta o doce do repositório
            return true;
        }).orElse(false); // Retorna false se o doce não for encontrado

    }
}
