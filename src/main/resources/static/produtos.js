// Utilitário para tratamento de erros e UI
const uiManager = {
    showLoading(target, buttonId = null) {
        if (buttonId) {
            // Se um botão específico foi informado, apenas adiciona loading nele
            const button = target.querySelector(`#${buttonId}`);
            if (button) {
                button.classList.add('loading');
                button.disabled = true;
            }
        } else {
            // Comportamento padrão: loading no container inteiro
            target.classList.add('loading');
        }
    },

    hideLoading(target, buttonId = null) {
        if (buttonId) {
            const button = target.querySelector(`#${buttonId}`);
            if (button) {
                button.classList.remove('loading');
                button.disabled = false;
            }
        } else {
            target.classList.remove('loading');
        }
    },

    showFeedback(target, type, message, duration = 3000) {
        const feedback = document.createElement('div');
        feedback.className = `feedback-message ${type}`;
        feedback.textContent = message;
        
        // Remove feedback anterior se existir
        target.querySelectorAll('.feedback-message').forEach(el => el.remove());
        
        // Adiciona novo feedback
        target.appendChild(feedback);
        
        // Anima entrada
        requestAnimationFrame(() => feedback.classList.add('show'));
        
        // Remove após o tempo especificado
        setTimeout(() => {
            feedback.classList.remove('show');
            setTimeout(() => feedback.remove(), 300);
        }, duration);
    },

    refreshList(containerId, items, template) {
        const container = document.getElementById(containerId);
        if (!container) return;

        container.innerHTML = '';
        
        if (items.length === 0) {
            this.showFeedback(container, 'info', 'Nenhum item encontrado');
            return;
        }

        items.forEach(item => {
            const element = template(item);
            container.appendChild(element);
        });
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

        if (formBusca) {
            formBusca.addEventListener('submit', (e) => this.buscarDoces(e));
        }
        if (formCadastro) {
            formCadastro.addEventListener('submit', (e) => this.cadastrarDoce(e));
        }
    },

    async carregarDoces() {
        try {
            const response = await fetch('/doces/listar');
            if (!response.ok) throw new Error('Erro ao carregar doces');
            
            const doces = await response.json();
            this.exibirDoces(doces);
        } catch (error) {
            console.error('Erro ao carregar doces:', error);
            const container = document.getElementById('listaDoces');
            if (container) {
                uiManager.showFeedback(container, 'error', 'Erro ao carregar doces. Tente novamente.');
            }
        }
    },

    async buscarDoces(e) {
        e.preventDefault();
        const form = e.target;
        const nome = document.getElementById('buscaDoceNome').value;
        
        try {
            uiManager.showLoading(form, 'btnBuscarDoce');
            
            const response = await fetch(`/doces?nome=${encodeURIComponent(nome)}`);
            if (!response.ok) throw new Error('Erro ao buscar doces');
            
            const doces = await response.json();
            this.exibirDoces(doces);
            
            if (doces.length > 0) {
                uiManager.showFeedback(form, 'success', 'Produtos encontrados com sucesso!');
            }
        } catch (error) {
            console.error('Erro ao buscar doces:', error);
            uiManager.showFeedback(form, 'error', 'Erro ao buscar doces. Tente novamente.');
        } finally {
            uiManager.hideLoading(form, 'btnBuscarDoce');
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
            uiManager.showLoading(form, 'btnCadastrarDoce');
            
            const response = await fetch('/doces/criar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(doce)
            });

            if (!response.ok) {
                throw new Error(await response.text() || 'Erro ao cadastrar doce');
            }

            uiManager.showFeedback(form, 'success', 'Doce cadastrado com sucesso!');
            form.reset();
            await this.carregarDoces();
        } catch (error) {
            uiManager.showFeedback(form, 'error', error.message || 'Erro ao cadastrar doce. Tente novamente.');
        } finally {
            uiManager.hideLoading(form, 'btnCadastrarDoce');
        }
    },

    exibirDoces(doces) {
        uiManager.refreshList('listaDoces', doces, (doce) => {
            const card = document.createElement('div');
            card.className = 'produto-card';
            card.innerHTML = `
                <h3>${doce.nome}</h3>
                <p>Sabor: ${doce.sabor}</p>
                <p>Preço: R$ ${doce.preco.toFixed(2)}</p>
                <p>Quantidade: ${doce.quantidade}</p>
            `;
            return card;
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
        if (formCadastro) {
            formCadastro.addEventListener('submit', (e) => this.cadastrarPlano(e));
        }
    },

    async carregarPlanos() {
        try {
            const response = await fetch('/planos/listar');
            if (!response.ok) throw new Error('Erro ao carregar planos');
            
            const planos = await response.json();
            this.exibirPlanos(planos);
        } catch (error) {
            console.error('Erro ao carregar planos:', error);
            const container = document.getElementById('listaPlanos');
            if (container) {
                uiManager.showFeedback(container, 'error', 'Erro ao carregar planos. Tente novamente.');
            }
        }
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
            uiManager.showLoading(form, 'btnCadastrarPlano');
            
            const response = await fetch('/planos/criar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(plano)
            });

            if (!response.ok) {
                throw new Error(await response.text() || 'Erro ao cadastrar plano');
            }

            uiManager.showFeedback(form, 'success', 'Plano cadastrado com sucesso!');
            form.reset();
            await this.carregarPlanos();
        } catch (error) {
            uiManager.showFeedback(form, 'error', error.message || 'Erro ao cadastrar plano. Tente novamente.');
        } finally {
            uiManager.hideLoading(form, 'btnCadastrarPlano');
        }
    },

    exibirPlanos(planos) {
        uiManager.refreshList('listaPlanos', planos, (plano) => {
            const card = document.createElement('div');
            card.className = 'produto-card plano-card';
            card.innerHTML = `
                <h3>${plano.nome}</h3>
                <p>Tipo: ${plano.tipo}</p>
                <p>Valor Mensal: R$ ${plano.valorMensal.toFixed(2)}</p>
                <button class="btn-contratar" id="btnContratar${plano.id}" data-id="${plano.id}">
                    Contratar
                </button>
            `;
            
            const btnContratar = card.querySelector('.btn-contratar');
            btnContratar.addEventListener('click', () => this.contratarPlano(plano.id));
            
            return card;
        });
    },

    async contratarPlano(planoId) {
        const btnContratar = document.getElementById(`btnContratar${planoId}`);
        if (!btnContratar) return;

        try {
            uiManager.showLoading(btnContratar.parentElement, `btnContratar${planoId}`);
            
            // Por enquanto vamos usar um ID fixo para teste
            // Em produção isso viria do usuário logado
            const clienteId = sessionStorage.getItem('userId') || "550e8400-e29b-41d4-a716-446655440000";
            
            const response = await fetch(`/planos/${planoId}/contratar?clienteId=${clienteId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(await response.text() || 'Erro ao contratar plano');
            }

            uiManager.showFeedback(
                btnContratar.parentElement,
                'success',
                'Plano contratado com sucesso!'
            );
        } catch (error) {
            uiManager.showFeedback(
                btnContratar.parentElement,
                'error',
                error.message || 'Erro ao contratar plano. Tente novamente.'
            );
        } finally {
            uiManager.hideLoading(btnContratar.parentElement, `btnContratar${planoId}`);
        }
    }
};

// Gerenciador de abas
const tabManager = {
    init() {
        document.querySelectorAll('.tab-button').forEach(button => {
            button.addEventListener('click', (e) => {
                const tabId = e.target.dataset.tab;
                this.switchTab(tabId);
            });
        });

        // Ativa primeira aba por padrão em cada seção
        document.querySelectorAll('section').forEach(section => {
            const firstTab = section.querySelector('.tab-button');
            if (firstTab) {
                this.switchTab(firstTab.dataset.tab);
            }
        });
    },

    switchTab(tabId) {
        if (!tabId) return;

        const tab = document.querySelector(`[data-tab="${tabId}"]`);
        if (!tab) return;

        const section = tab.closest('section');
        if (!section) return;

        // Remove active de todas as abas na seção
        section.querySelectorAll('.tab-button').forEach(btn => {
            btn.classList.remove('active');
        });
        section.querySelectorAll('.tab-content').forEach(content => {
            content.classList.remove('active');
        });

        // Ativa aba selecionada
        tab.classList.add('active');
        const content = document.getElementById(tabId);
        if (content) {
            content.classList.add('active');
        }
    }
};

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    doceManager.init();
    planoManager.init();
    tabManager.init();
});
