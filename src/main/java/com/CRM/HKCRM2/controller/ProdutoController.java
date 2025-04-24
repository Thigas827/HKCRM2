
package com.CRM.HKCRM2.controller; // Importações necessárias para o controlador de produtos

import java.util.List;
import java.util.Optional;
import com.CRM.HKCRM2.dtos.ProdutoDtos;
import com.CRM.HKCRM2.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.CRM.HKCRM2.repositories.ProdutoRepository;
// -------------------------------------------------------------Classe ProdutoController é responsável por gerenciar os endpoints REST relacionados à entidade Produto,
//------------------------------------------------------------- permitindo operações como listar, buscar, salvar, atualizar e deletar produtos.



@RestController                                                 // Indica que esta classe é um controlador REST
@RequestMapping("/produto")                                     // Define o caminho base para os endpoints deste controlador
public class ProdutoController {


    @Autowired                                                  // Injeção de dependência do repositório
    ProdutoRepository repository;                               // Repositório para acessar os dados do produto

    // Método para listar todos os produtos
    @GetMapping                                               
    public ResponseEntity getAll() {
        List<Produto> ListProdutos = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(ListProdutos);



    }
    // Método para obter um produto específico pelo ID
    @GetMapping("/{id}")                                         
    public ResponseEntity getById(@PathVariable(value = "id") Integer id) {                             // Recebe o ID do produto como parâmetro
    
            Optional produto = repository.findById(id);                                                 // Verifica se o produto existe no banco de dados

            if(produto.isEmpty()){                                                                      //condição para verificar se o produto existe
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado"); // Se o produto não for encontrado, retorna 404
            
            }
            
            return ResponseEntity.status(HttpStatus.FOUND).body(produto.get());                         // Se o produto for encontrado, retorna o produto com status 302

    }

    // Método para salvar um novo produto
    @PostMapping
    public ResponseEntity save(@RequestBody ProdutoDtos dtos) {                                         // Recebe os dados do produto no corpo da requisição
        var produto = new Produto();                                                                    // Cria um novo objeto Produto
        BeanUtils.copyProperties(dtos, produto);                                                        // Copia as propriedades do DTO para o objeto Produto
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(produto));                // Salva o produto no banco de dados e retorna o produto salvo com status 201
    }

    // Método para atualizar um produto existente
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        Optional<Produto> produto = repository.findById(id);                                           // Verifica se o produto existe no banco de dados

            if(produto.isEmpty()){                                                                      //condição para verificar se o produto existe
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");      // Se o produto não for encontrado, retorna 404
            
        }
        repository.delete(produto.get());                                                              // Deleta o produto do banco de dados
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso"); 

    }

    // Método para atualizar um produto existente
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Integer id, @RequestBody ProdutoDtos dtos) {
        Optional<Produto> produto = repository.findById(id);                                            // Verifica se o produto existe no banco de dados

            if(produto.isEmpty()){                                                                      //condição para verificar se o produto existe
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");      // Se o produto não for encontrado, retorna 404
            
        }
        var produtoModel = produto.get();                                                               // Se o produto for encontrado, obtém o produto
        BeanUtils.copyProperties(dtos, produtoModel);                                                   // Copia as propriedades do DTO para o produto existente                                              
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(produtoModel));                // Atualiza o produto no banco de dados e retorna o produto atualizado

    }


}
