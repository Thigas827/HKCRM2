package com.CRM.HKCRM2.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CRM.HKCRM2.dtos.CompraDtos;
import com.CRM.HKCRM2.dtos.ItemDtos;
import com.CRM.HKCRM2.dtos.CompraDetalhesDto;
import com.CRM.HKCRM2.dtos.ItemCompraDetalhesDto;
import com.CRM.HKCRM2.model.Compra;
import com.CRM.HKCRM2.model.CompraItem;
import com.CRM.HKCRM2.model.Produto;
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.repositories.CompraItemRepository;
import com.CRM.HKCRM2.repositories.CompraRepository;
import com.CRM.HKCRM2.repositories.ProdutoRepository;
import com.CRM.HKCRM2.repositories.UIUsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável por toda a lógica de negócio relacionada a compras/vendas.
 * 
 * Este serviço gerencia:
 * - Registro de novas compras
 * - Cálculo de valores totais
 * - Gestão dos itens de compra
 * - Consulta de histórico de compras
 * 
 * O serviço utiliza transações para garantir a consistência dos dados,
 * especialmente importante ao registrar uma compra com múltiplos itens.
 */
@Service
public class CompraService {

    @Autowired private CompraRepository compraRepo; 
    @Autowired private CompraItemRepository compraItemRepo;
    @Autowired private ProdutoRepository produtoRepo;
    @Autowired private UIUsuarioRepository usuarioRepo;

    @Transactional
    public Compra registrarCompra(CompraDtos dto) {
        // 1) Busca o usuário (cliente)
        UsuarioMod cliente = usuarioRepo.findById(dto.clienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        // 2) Cria a Compra inicial com total zero (para gerar ID)
        Compra compra = new Compra(cliente, BigDecimal.ZERO);
        compra = compraRepo.save(compra);

        // 3) Para cada item do DTO, busca o produto e cria CompraItem
        BigDecimal total = BigDecimal.ZERO;
        for (ItemDtos itemDto : dto.itens()) {
            Produto produto = produtoRepo.findById(itemDto.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            // Se Produto.preco for Double, converta para BigDecimal assim:
            BigDecimal precoUnit = BigDecimal.valueOf(produto.getPreco());

            BigDecimal subTotal = precoUnit
                .multiply(BigDecimal.valueOf(itemDto.quantidade()));

            total = total.add(subTotal);

            CompraItem compraItem = new CompraItem(
                compra,
                produto,
                itemDto.quantidade(),   // não ItemDtos.quantidade()
                precoUnit
            );
            compraItemRepo.save(compraItem);  // repo correto
        }

        // 4) Atualiza e salva o valor total da compra
        compra.setValorTotal(total);
        return compraRepo.save(compra);
    }

    public List<Compra> listarPorCliente(UUID clienteId) {
        return compraRepo.findByClienteIdOrderByDataCompraDesc(clienteId);
    }

    public CompraDetalhesDto buscarDetalhesCompra(Integer compraId) {
        Compra compra = compraRepo.findById(compraId)
            .orElseThrow(() -> new EntityNotFoundException("Compra não encontrada"));

        List<ItemCompraDetalhesDto> itensDetalhes = compra.getItens().stream()
            .map(item -> new ItemCompraDetalhesDto(
                item.getProdutoNome(),
                item.getQuantidade(),
                item.getPrecoUnit(),
                item.getPrecoUnit().multiply(BigDecimal.valueOf(item.getQuantidade()))
            ))
            .toList();

        return new CompraDetalhesDto(
            compra.getId(),
            compra.getDataCompra(),
            compra.getValorTotal(),
            itensDetalhes
        );
    }

    public List<CompraDetalhesDto> listarDetalhesComprasPorCliente(UUID clienteId) {
        List<Compra> compras = compraRepo.findByClienteIdOrderByDataCompraDesc(clienteId);
        
        return compras.stream()
            .map(compra -> {
                List<ItemCompraDetalhesDto> itensDetalhes = compra.getItens().stream()
                    .map(item -> new ItemCompraDetalhesDto(
                        item.getProdutoNome(),
                        item.getQuantidade(),
                        item.getPrecoUnit(),
                        item.getPrecoUnit().multiply(BigDecimal.valueOf(item.getQuantidade()))
                    ))
                    .toList();

                return new CompraDetalhesDto(
                    compra.getId(),
                    compra.getDataCompra(),
                    compra.getValorTotal(),
                    itensDetalhes
                );
            })
            .toList();
    }
}
