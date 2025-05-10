package com.CRM.HKCRM2.Service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CRM.HKCRM2.dtos.ProdutoDto;
import com.CRM.HKCRM2.model.Produto;
import com.CRM.HKCRM2.repositories.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto criar(ProdutoDto dto) {
        Produto produto = new Produto();
        BeanUtils.copyProperties(dto, produto);
        return repository.save(produto);
    }
}
