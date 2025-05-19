// produtos.js
// URL base do seu backend
const API = 'http://localhost:8080';  // ou a URL correta
// elementos de login
const formLogin    = document.getElementById('formLogin');
const loginEmail   = document.getElementById('loginEmail');
const loginSenha   = document.getElementById('loginSenha');
const loginMsg     = document.getElementById('loginMsg');

// elementos de cadastro cliente/compra
const formCadastro             = document.getElementById('formCadastro');
const nomeCliente              = document.getElementById('nomeCliente');
const emailCliente             = document.getElementById('emailCliente');
const endereco                 = document.getElementById('endereco');
const telefone                 = document.getElementById('telefone');
const addDoceSelect            = document.getElementById('addDoceSelect');
const itensCompraSelects       = document.getElementById('itensCompraSelects');
const cadMsg                   = document.getElementById('cadastroMsg');

// histórico
const formHistorico    = document.getElementById('formHistorico');
const clienteSelect    = document.getElementById('clienteSelect');
const historicoMsg     = document.getElementById('historicoMsg');
const historicoCompras = document.getElementById('historicoCompras');
// ============================
// 1) Utilitários de UI e validação
// ============================
const uiManager = {
  showLoading(target, buttonId = null) {
    if (buttonId) {
      const btn = target.querySelector(`#${buttonId}`);
      btn?.classList.add('loading');
      if (btn) btn.disabled = true;
    } else {
      target.classList.add('loading');
    }
  },
  hideLoading(target, buttonId = null) {
    if (buttonId) {
      const btn = target.querySelector(`#${buttonId}`);
      btn?.classList.remove('loading');
      if (btn) btn.disabled = false;
    } else {
      target.classList.remove('loading');
    }
  },
  showFeedback(target, type, message, duration = 3000) {
    const f = document.createElement('div');
    f.className = `feedback-message ${type}`;
    f.textContent = message;
    target.querySelectorAll('.feedback-message').forEach(el => el.remove());
    target.appendChild(f);
    requestAnimationFrame(() => f.classList.add('show'));
    setTimeout(() => {
      f.classList.remove('show');
      setTimeout(() => f.remove(), 300);
    }, duration);
  }
};

const formValidator = {
  validateDoce(doce) {
    if (!doce.nome || doce.nome.trim().length < 3)
      throw new Error('Nome do doce deve ter pelo menos 3 caracteres');
  },
  validatePlano(plano) {
    if (!plano.nome || plano.nome.trim().length < 3)
      throw new Error('Nome do plano deve ter pelo menos 3 caracteres');
  }
};

// ============================
// 2) Cadastro Dinâmico (Doce x Academia)
// ============================
const cadastroManager = {
  init() {
    this.form = document.getElementById('formCadastro');
    this.tipoSel = document.getElementById('tipoCadastro');
    this.prodContainer = document.getElementById('produtosContainer');
    this.prodLabel = document.getElementById('labelProduto');
    this.prodSelect = document.getElementById('produtoSelect');
    this.msgBox = document.getElementById('cadastroMsg');

    this.tipoSel.addEventListener('change', () => this.onTipoChange());
    this.form.addEventListener('submit', e => this.onSubmit(e));
  },

  async onTipoChange() {
    const tipo = this.tipoSel.value; // 'doce' ou 'academia'
    if (!tipo) {
      this.prodContainer.classList.add('hidden');
      return;
    }

    // ajustar label
    this.prodLabel.textContent = tipo === 'doce'
      ? 'doce'
      : 'plano de academia';
    this.prodContainer.classList.remove('hidden');

    // buscar lista
    const url = tipo === 'doce'
      ? '/doces/listar'
      : '/planos/listar';
    try {
      uiManager.showLoading(this.prodContainer);
      const res = await fetch(url);
      if (!res.ok) throw new Error('Falha ao carregar itens');
      const items = await res.json();

      // preencher select
      this.prodSelect.innerHTML = '<option value="">Selecione…</option>';
      items.forEach(i => {
        const o = document.createElement('option');
        o.value = i.id;
        o.textContent = tipo === 'doce'
          ? `${i.nome} (R$ ${i.preco.toFixed(2)})`
          : `${i.nome} – ${i.tipo} (R$ ${i.valorMensal.toFixed(2)})`;
        this.prodSelect.append(o);
      });
    } catch (err) {
      uiManager.showFeedback(this.prodContainer, 'error', err.message);
    } finally {
      uiManager.hideLoading(this.prodContainer);
    }
  },

  async onSubmit(e) {
    e.preventDefault();
    const tipo = this.tipoSel.value;
    const nome = document.getElementById('nomeCliente').value.trim();
    const email = document.getElementById('emailCliente').value.trim();

    // simples validação
    if (!tipo || !nome || !email) {
      this.msgBox.textContent = 'Preencha todos os campos!';
      return;
    }

    // montar payload
    try {
      let res, body;
      if (tipo === 'doce') {
        const produtoId = +this.prodSelect.value;
        const dto = { cliente: { nome, email }, itens: [{ produtoId, quantidade: 1 }] };

        res = await fetch('/compras', { // criar nova compra
          method: 'POST',
          headers: {'Content-Type':'application/json'},
          body: JSON.stringify(dto)
        });
        if (!res.ok) throw new Error(await res.text());
        body = await res.json();
        this.msgBox.textContent = 'Você cadastrou uma nova compra!';
      } else {
        const planoId = +this.prodSelect.value;
        // aqui, assumimos que o cliente já existe e seu ID está em sessionStorage
        const clienteId = sessionStorage.getItem('userId');
        res = await fetch(`/planos/${planoId}/contratar?clienteId=${clienteId}`, {
          method: 'POST'
        });
        if (!res.ok) throw new Error(await res.text());
        this.msgBox.textContent = 'Você cadastrou um novo aluno!';
      }
      uiManager.showFeedback(this.form, 'success', this.msgBox.textContent);
      this.form.reset();
      this.prodContainer.classList.add('hidden');
    } catch (err) {
      uiManager.showFeedback(this.form, 'error', err.message);
    }
  }
};
// ------------ CADASTRO DOCE + PLANO ------------
document.getElementById('formCadastroDoce').onsubmit = async e => {
  e.preventDefault();

const dto = {
 nome:   document.getElementById('nomeDoce').value,
  sabor:  document.getElementById('saborDoce').value,
  preco:  parseFloat(document.getElementById('precoDoce').value),
  quantidade: parseInt(document.getElementById('quantidadeDoce').value, 10)
  };

  try {
    // Cria o doce
    const res = await fetch(`${API}/doces/criar`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(dto)
    });
    if (!res.ok) throw new Error(await res.text());
    uiManager.showFeedback(formCadastroDoce, 'success', 'Doce cadastrado!');
    formCadastroDoce.reset();
    // Atualiza lista de doces
    await refreshLista('/doces/listar', 'listaDoces');
  } catch (err) {
    uiManager.showFeedback(formCadastroDoce, 'error', err.message);
  }
};

document.getElementById('formPlano').onsubmit = async e => {
  e.preventDefault();

  const dto = {
    nome: planoNome.value,
    tipoPlano: planoTipo.value,
    valorMensalidade: parseFloat(planoValor.value)
  };

  try {
    // Cria o plano
    const res = await fetch(`${API}/planos/criar`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(dto)
    });
    if (!res.ok) throw new Error(await res.text());
    uiManager.showFeedback(formPlano, 'success', 'Plano cadastrado!');
    formPlano.reset();
    // Atualiza lista de planos
    await refreshLista('/planos/listar', 'listaPlanos');
  } catch (err) {
    uiManager.showFeedback(formPlano, 'error', err.message);
  }
};

// ajusta o nome do campo do plano na exibição
async function refreshLista(url, containerId) {
  try {
    const res = await fetch(API + url);
    if (!res.ok) throw new Error('Falha ao carregar dados');
    const items = await res.json();
    document.getElementById(containerId).innerHTML = items.map(o=>`
      <div class="produto-card">
        <h4>${o.nome}</h4>
        <p>${o.sabor || o.tipoPlano}</p>
        <p>R$ ${(o.preco ?? o.valorMensalidade).toFixed(2)}</p>
      </div>
    `).join('');
  } catch (err) {
    console.error(err);
    document.getElementById(containerId).innerHTML = `<p class="error">Erro: ${err.message}</p>`;
  }
}

// ------------ FIM CADASTRO DOCE + PLANO ------------

// no final do DOMContentLoaded, adicione:
window.addEventListener('DOMContentLoaded', () => {
  // existentes...
  refreshLista('/doces/listar', 'listaDoces');
  refreshLista('/planos/listar', 'listaPlanos');
});

// ============================
// 3) Inicialização Geral
// ============================
document.addEventListener('DOMContentLoaded', () => {
  cadastroManager.init();
  themeManager.init();
    // Inicializa o manager de produtos

});
