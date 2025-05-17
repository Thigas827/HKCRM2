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
            const response = await fetch(`/api/doces?nome=${encodeURIComponent(nome)}`);
            const doces = await response.json();
            this.exibirDoces(doces);
        } catch (error) {
            console.error('Erro ao buscar doces:', error);
        }
    },

    async cadastrarDoce(e) {
        e.preventDefault();
        const doce = {
            nome: document.getElementById('nomeDoce').value,
            sabor: document.getElementById('saborDoce').value,
            preco: parseFloat(document.getElementById('precoDoce').value),
            quantidade: parseInt(document.getElementById('quantidadeDoce').value)
        };

        try {
            const response = await fetch('/api/doces', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(doce)
            });

            if (response.ok) {
                formManager.showFeedback(e.target, 'success', 'Doce cadastrado com sucesso!');
                e.target.reset();
                this.carregarDoces();
            } else {
                formManager.showFeedback(e.target, 'error', 'Erro ao cadastrar doce');
            }
        } catch (error) {
            console.error('Erro ao cadastrar doce:', error);
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
        const plano = {
            nome: document.getElementById('nomePlano').value,
            tipo: document.getElementById('tipoPlano').value,
            valorMensal: parseFloat(document.getElementById('valorPlano').value)
        };

        try {
            const response = await fetch('/api/planos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(plano)
            });

            if (response.ok) {
                formManager.showFeedback(e.target, 'success', 'Plano cadastrado com sucesso!');
                e.target.reset();
                this.carregarPlanos();
            } else {
                formManager.showFeedback(e.target, 'error', 'Erro ao cadastrar plano');
            }
        } catch (error) {
            console.error('Erro ao cadastrar plano:', error);
        }
    },

    async carregarPlanos() {
        try {
            const response = await fetch('/api/planos');
            const planos = await response.json();
            this.exibirPlanos(planos);
        } catch (error) {
            console.error('Erro ao carregar planos:', error);
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
        // Implementar lógica de contratação
        console.log('Contratar plano:', planoId);
    }
};

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    doceManager.init();
    planoManager.init();
});
