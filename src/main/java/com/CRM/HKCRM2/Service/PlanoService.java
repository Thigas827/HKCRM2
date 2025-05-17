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
import com.CRM.HKCRM2.model.UsuarioMod;
import com.CRM.HKCRM2.dtos.PlanoDtos;
import com.CRM.HKCRM2.dtos.CompraDtos;
import com.CRM.HKCRM2.dtos.ItemDtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar operações relacionadas a planos.
 * Inclui operações CRUD e contratação de planos.
 */
@Service
public class PlanoService {

    @Autowired
    private PlanoRepository repository;

    @Autowired
    private CompraService compraService;

    @Autowired
    private UIUsuarioRepository usuarioRepository;

    /**
     * Lista todos os planos cadastrados.
     */
    public List<Plano> listAll() {
        return repository.findAll();
    }

    /**
     * Busca um plano pelo ID.
     */
    public Optional<Plano> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do plano não pode ser nulo");
        }
        return repository.findById(id);
    }

    /**
     * Cria um novo plano a partir do DTO.
     * Calcula automaticamente as datas se não forem fornecidas.
     */
    @Transactional
    public Plano create(PlanoDtos dto) {
        validarPlanoDto(dto);

        Plano plano = new Plano();
        BeanUtils.copyProperties(dto, plano);

        // Se não foi fornecida data de início, usa a data atual
        if (plano.getDataInicio() == null) {
            plano.setDataInicio(LocalDate.now());
        }

        // Se não foi fornecida data fim, calcula com base no tipo do plano
        if (plano.getDataFim() == null) {
            plano.setDataFim(calcularDataFim(plano.getDataInicio(), plano.getTipoPlano()));
        }

        // Se não foi fornecida data de vencimento, calcula 30 dias após a data fim
        if (plano.getDataVencimento() == null) {
            plano.setDataVencimento(plano.getDataFim().plusDays(30));
        }

        // Define o preço como o valor da mensalidade
        if (plano.getPreco() == null) {
            plano.setPreco(plano.getValorMensalidade());
        }

        return repository.save(plano);
    }

    /**
     * Atualiza um plano existente.
     */
    @Transactional
    public Optional<Plano> update(Integer id, PlanoDtos dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID do plano não pode ser nulo");
        }
        validarPlanoDto(dto);

        return repository.findById(id).map(plano -> {
            // Preserva os dados que não devem ser atualizados
            LocalDate dataInicio = plano.getDataInicio();
            
            // Atualiza os dados do plano
            BeanUtils.copyProperties(dto, plano);
            
            // Restaura a data de início original
            plano.setDataInicio(dataInicio);
            
            // Recalcula as datas se necessário
            if (dto.dataFim() == null) {
                plano.setDataFim(calcularDataFim(plano.getDataInicio(), plano.getTipoPlano()));
            }
            if (dto.dataVencimento() == null) {
                plano.setDataVencimento(plano.getDataFim().plusDays(30));
            }

            return repository.save(plano);
        });
    }

    /**
     * Remove um plano do sistema.
     */
    @Transactional
    public boolean delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do plano não pode ser nulo");
        }

        return repository.findById(id).map(plano -> {
            repository.delete(plano);
            return true;
        }).orElse(false);
    }

    /**
     * Realiza a contratação de um plano por um cliente.
     */
    @Transactional
    public void contratar(Integer planoId, UUID clienteId) {
        if (planoId == null || clienteId == null) {
            throw new IllegalArgumentException("ID do plano e do cliente são obrigatórios");
        }        // Verifica se o plano existe e está ativo
        Plano plano = repository.findById(planoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado"));

        // Verifica se o cliente existe e está ativo
        UsuarioMod cliente = usuarioRepository.findById(clienteId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        
        // Verifica se o cliente está apto para contratar
        verificarClienteApto(cliente, plano);

        // Cria um DTO de item para o plano
        ItemDtos itemDto = new ItemDtos(planoId, 1);
        
        // Cria um DTO de compra com o cliente e o item
        CompraDtos compraDtos = new CompraDtos(clienteId, List.of(itemDto));

        // Registra a compra do plano
        compraService.registrarCompra(compraDtos);
    }

    /**
     * Verifica se o cliente está apto para contratar o plano.
     * Esta é uma validação básica que pode ser expandida conforme as regras de negócio.
     */
    private void verificarClienteApto(UsuarioMod cliente, Plano plano) {
        // TODO: Implementar regras de negócio específicas
        // Exemplos de validações que podem ser adicionadas:
        // - Verificar se o cliente já possui um plano ativo
        // - Verificar limite de planos por cliente
        // - Verificar restrições financeiras
        // - Verificar idade mínima para certos tipos de plano
        // - etc.
        
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente inválido");
        }
        
        if (plano == null) {
            throw new IllegalArgumentException("Plano inválido");
        }
        
        // Por enquanto, apenas verifica se o cliente tem um endereço cadastrado
        if (cliente.getEndereco() == null || cliente.getEndereco().trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Cliente precisa ter um endereço cadastrado para contratar um plano"
            );
        }
    }

    /**
     * Valida os dados do DTO de plano.
     */
    private void validarPlanoDto(PlanoDtos dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO do plano não pode ser nulo");
        }
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano é obrigatório");
        }
        if (dto.tipoPlano() == null || dto.tipoPlano().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do plano é obrigatório");
        }
        if (dto.valorMensalidade() == null || dto.valorMensalidade() <= 0) {
            throw new IllegalArgumentException("Valor da mensalidade deve ser maior que zero");
        }
    }

    /**
     * Calcula a data de fim do plano com base no tipo.
     */
    private LocalDate calcularDataFim(LocalDate dataInicio, String tipoPlano) {
        if (dataInicio == null || tipoPlano == null) {
            throw new IllegalArgumentException("Data de início e tipo do plano são obrigatórios");
        }

        return switch (tipoPlano.toUpperCase()) {
            case "MENSAL" -> dataInicio.plusMonths(1);
            case "TRIMESTRAL" -> dataInicio.plusMonths(3);
            case "SEMESTRAL" -> dataInicio.plusMonths(6);
            case "ANUAL" -> dataInicio.plusYears(1);
            default -> throw new IllegalArgumentException("Tipo de plano inválido: " + tipoPlano);
        };
    }
}
