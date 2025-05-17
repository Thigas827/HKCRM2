// Utilitário para tratamento de erros
const errorHandler = {
    async handleResponse(response, successMessage) {
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro na operação');
        }
        return response.json().catch(() => ({ message: successMessage }));
    },

    showError(target, error) {
        formManager.showFeedback(
            target, 
            'error', 
            error.message || 'Ocorreu um erro. Tente novamente.'
        );
    }
};

// Validação de formulários
const formValidator = {
    validateDoce(doce) {
        if (!doce.nome || doce.nome.trim().length < 3) {
            throw new Error('Nome do doce deve ter pelo menos 3 caracteres');
        }
        if (!doce.sabor || doce.sabor.trim().length < 3) {
            throw new Error('Sabor deve ter pelo menos 3 caracteres');
        }
        if (!doce.preco || doce.preco <= 0) {
            throw new Error('Preço deve ser maior que zero');
        }
        if (!doce.quantidade || doce.quantidade < 0) {
            throw new Error('Quantidade não pode ser negativa');
        }
    },

    validatePlano(plano) {
        if (!plano.nome || plano.nome.trim().length < 3) {
            throw new Error('Nome do plano deve ter pelo menos 3 caracteres');
        }
        if (!plano.tipo) {
            throw new Error('Tipo do plano é obrigatório');
        }
        if (!plano.valorMensal || plano.valorMensal <= 0) {
            throw new Error('Valor mensal deve ser maior que zero');
        }
    }
};

// Gerenciador de Doces
const doceManager = {
    init() {
        this.setupFormsDoces();
        this.carregarDoces();
    },

    setupFormsDoces() {
        const formBusca = document.getElementById('formBuscaDoce');
        const formCadastro = document.getElementById('formCadastroDoce');

        formBusca.addEventListener('submit', (e) => this.buscarDoces(e));
        formCadastro.addEventListener('submit', (e) => this.cadastrarDoce(e));
    },

    async buscarDoces(e) {
        e.preventDefault();
        const nome = document.getElementById('buscaDoceNome').value;
        try {
            const response = await fetch(`/doces?nome=${encodeURIComponent(nome)}`);
            const doces = await response.json();
            this.exibirDoces(doces);
        } catch (error) {
            console.error('Erro ao buscar doces:', error);
            formManager.showFeedback(e.target, 'error', 'Erro ao buscar doces. Tente novamente.');
        }
    },

    async cadastrarDoce(e) {
        e.preventDefault();
        const form = e.target;
        const doce = {
            nome: document.getElementById('nomeDoce').value,
            sabor: document.getElementById('saborDoce').value,
            preco: parseFloat(document.getElementById('precoDoce').value),
            quantidade: parseInt(document.getElementById('quantidadeDoce').value)
        };

        try {
            formValidator.validateDoce(doce);
            form.classList.add('loading');
            
            const response = await fetch('/doces/criar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(doce)
            });

            await errorHandler.handleResponse(response, 'Doce cadastrado com sucesso!');
            formManager.showFeedback(form, 'success', 'Doce cadastrado com sucesso!');
            form.reset();
            this.carregarDoces();
        } catch (error) {
            errorHandler.showError(form, error);
        } finally {
            form.classList.remove('loading');
        }
    },

    exibirDoces(doces) {
        const lista = document.getElementById('listaDoces');
        lista.innerHTML = '';

        doces.forEach(doce => {
            const card = document.createElement('div');
            card.className = 'produto-card';
            card.innerHTML = `
                <h3>${doce.nome}</h3>
                <p>Sabor: ${doce.sabor}</p>
                <p>Preço: R$ ${doce.preco.toFixed(2)}</p>
                <p>Quantidade: ${doce.quantidade}</p>
            `;
            lista.appendChild(card);
        });
    }
};

// Gerenciador de Planos
const planoManager = {
    init() {
        this.setupFormsPlanos();
        this.carregarPlanos();
    },

    setupFormsPlanos() {
        const formCadastro = document.getElementById('formCadastroPlano');
        formCadastro.addEventListener('submit', (e) => this.cadastrarPlano(e));
    },

    async cadastrarPlano(e) {
        e.preventDefault();
        const form = e.target;
        const plano = {
            nome: document.getElementById('nomePlano').value,
            tipo: document.getElementById('tipoPlano').value,
            valorMensal: parseFloat(document.getElementById('valorPlano').value)
        };

        try {
            formValidator.validatePlano(plano);
            form.classList.add('loading');
            
            const response = await fetch('/planos/criar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(plano)
            });

            await errorHandler.handleResponse(response, 'Plano cadastrado com sucesso!');
            formManager.showFeedback(form, 'success', 'Plano cadastrado com sucesso!');
            form.reset();
            this.carregarPlanos();
        } catch (error) {
            errorHandler.showError(form, error);
        } finally {
            form.classList.remove('loading');
        }
    },

    async carregarPlanos() {
        try {
            const response = await fetch('/planos/listar');
            const planos = await response.json();
            this.exibirPlanos(planos);
        } catch (error) {
            console.error('Erro ao carregar planos:', error);
            formManager.showFeedback(document.getElementById('planos-content'), 'error', 'Erro ao carregar planos. Tente novamente.');
        }
    },

    exibirPlanos(planos) {
        const lista = document.getElementById('listaPlanos');
        lista.innerHTML = '';

        planos.forEach(plano => {
            const card = document.createElement('div');
            card.className = 'produto-card plano-card';
            card.innerHTML = `
                <h3>${plano.nome}</h3>
                <p>Tipo: ${plano.tipo}</p>
                <p>Valor Mensal: R$ ${plano.valorMensal.toFixed(2)}</p>
                <button class="btn-contratar" data-id="${plano.id}">Contratar</button>
            `;
            lista.appendChild(card);
        });

        // Adiciona listeners para botões de contratar
        lista.querySelectorAll('.btn-contratar').forEach(btn => {
            btn.addEventListener('click', () => this.contratarPlano(btn.dataset.id));
        });
    },

    async contratarPlano(planoId) {
        try {
            // Por enquanto vamos usar um ID fixo para teste
            // Em produção isso viria do usuário logado
            const clienteId = "550e8400-e29b-41d4-a716-446655440000"; // UUID exemplo
            
            const response = await fetch(`/planos/${planoId}/contratar?clienteId=${clienteId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                formManager.showFeedback(
                    document.getElementById('planos-content'),
                    'success',
                    'Plano contratado com sucesso!'
                );
            } else {
                const error = await response.text();
                throw new Error(error || 'Erro ao contratar plano');
            }
        } catch (error) {
            formManager.showFeedback(
                document.getElementById('planos-content'),
                'error',
                error.message || 'Erro ao contratar plano. Tente novamente.'
            );
        }
    }
};

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    doceManager.init();
    planoManager.init();
});
