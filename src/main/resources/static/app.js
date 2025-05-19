// URL base do seu backend
const API = 'http://localhost:8080';

// Captura de elementos fixos
const formLogin           = document.getElementById('formLogin');
const loginEmail          = document.getElementById('loginEmail');
const loginSenha          = document.getElementById('loginSenha');
const loginMsg            = document.getElementById('loginMsg');
const formCadastro        = document.getElementById('formCadastro');
const nomeCliente         = document.getElementById('nomeCliente');
const emailCliente        = document.getElementById('emailCliente');
const endereco            = document.getElementById('endereco');
const telefone            = document.getElementById('telefone');
const addDoceSelect       = document.getElementById('addDoceSelect');
const itensCompraSelects  = document.getElementById('itensCompraSelects');
const cadMsg              = document.getElementById('cadastroMsg');
const formHistorico       = document.getElementById('formHistorico');
const clienteSelect       = document.getElementById('clienteSelect');
const historicoMsg        = document.getElementById('historicoMsg');
const historicoCompras    = document.getElementById('historicoCompras');
const themeBtn            = document.getElementById('toggleTheme');

// Navegação entre seções
document.querySelectorAll('nav a').forEach(a => {
  a.onclick = () => {
    document.querySelectorAll('section').forEach(s => s.classList.remove('active'));
    document.getElementById(a.dataset.target).classList.add('active');
  };
});

// Modo escuro
themeBtn.onclick = () => {
  document.body.classList.toggle('dark');
  themeBtn.textContent = document.body.classList.contains('dark') ? 'Modo Claro' : 'Modo Escuro';
};

// ------------ LOGIN ------------
formLogin.onsubmit = async e => {
  e.preventDefault();
  const resp = await fetch(`${API}/usuarios/login`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({
      email: loginEmail.value,
      senha: loginSenha.value
    })
  });
  if (resp.ok) {
    loginMsg.textContent = '✔️ Logado!';
  } else {
    loginMsg.textContent = '❌ E-mail ou senha inválidos';
  }
};

// ------------ CADASTRO CLIENTE + COMPRA ------------
addDoceSelect.onclick = addItem;
formCadastro.onsubmit = cadastrarClienteCompra;

async function addItem(){
  const div = document.createElement('div');
  div.innerHTML = `
    <select class="prodSel"></select>
    <input type="number" class="qtde" value="1" min="1">
    <button class="rem">X</button>`;
  div.querySelector('.rem').onclick = () => div.remove();
  itensCompraSelects.append(div);
  await loadProdutos(div.querySelector('.prodSel'));
}

async function loadProdutos(sel){
  const r = await fetch(`${API}/doces/listar`);
  const ps = await r.json();
  sel.innerHTML = ps.map(p =>
    `<option value="${p.id}">${p.nome} (R$${p.preco.toFixed(2)})</option>`
  ).join('');
}

async function cadastrarClienteCompra(e){
  e.preventDefault();
  // 1) cria cliente
  const cliente = {
    nome: nomeCliente.value,
    email: emailCliente.value,
    senha: 'Temp123!',
    endereco: endereco.value,
    telefone: telefone.value,
    roles: [{nome:'ROLE_CLIENTE'}]
  };
  const cr = await fetch(`${API}/usuarios/criar`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(cliente)
  });
  if (!cr.ok) {
    cadMsg.textContent = '❌ Erro ao cadastrar cliente.';
    return;
  }
  const user = await cr.json();
  // 2) registra compra
  const itens = Array.from(itensCompraSelects.children).map(d => ({
    produtoId: +d.querySelector('.prodSel').value,
    quantidade: +d.querySelector('.qtde').value
  }));
  const cp = await fetch(`${API}/compras`, {
    method: 'POST',
    headers: {
      'Content-Type':'application/json',
      'Authorization': 'Basic ' + btoa(`${user.email}:Temp123!`)
    },
    body: JSON.stringify({clienteId: user.id, itens})
  });
  if (cp.ok) {
    cadMsg.textContent = '✅ Venda registrada!';
    formCadastro.reset();
    itensCompraSelects.innerHTML = '';
  } else {
    cadMsg.textContent = '❌ Erro ao registrar compra.';
  }
}

// ------------ HISTÓRICO ------------
formHistorico.onsubmit = async e => {
  e.preventDefault();
  const clienteId = clienteSelect.value;
  if (!clienteId) {
    historicoMsg.textContent = 'Selecione um cliente.';
    return;
  }
  const rc = await fetch(`${API}/compras?clienteId=${clienteId}`);
  const list = await rc.json();
  historicoCompras.innerHTML = list.length
    ? list.map(c => `
        <div class="compra-card">
          <div class="compra-header">
            <strong>#${c.id}</strong>
            ${new Date(c.dataCompra).toLocaleString()}
            <span>R$ ${c.valorTotal.toFixed(2)}</span>
          </div>
        </div>
      `).join('')
    : 'Nenhuma compra.';
};

// ------------ DOCES & PLANOS ------------
const formDoce  = document.getElementById('formCadastroDoce');
const formPlano = document.getElementById('formCadastroPlano');

formDoce.onsubmit = async e => {
  e.preventDefault();
  const dto = {
    nome: document.getElementById('nomeDoce').value,
    sabor: document.getElementById('saborDoce').value,
    preco: parseFloat(document.getElementById('precoDoce').value),
    quantidade: parseInt(document.getElementById('quantidadeDoce').value, 10)
  };
  await fetch(`${API}/doces/criar`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(dto)
  });
  await refreshLista('/doces/listar','listaDoces');
  formDoce.reset();
};

formPlano.onsubmit = async e => {
  e.preventDefault();
  const dto = {
    nome: document.getElementById('nomePlano').value,
    tipo: document.getElementById('tipoPlano').value,
    valorMensal: parseFloat(document.getElementById('valorPlano').value)
  };
  await fetch(`${API}/planos/criar`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(dto)
  });
  await refreshLista('/planos/listar','listaPlanos');
  formPlano.reset();
};

async function refreshLista(path, containerId){
  const r = await fetch(API + path);
  const items = await r.json();
  document.getElementById(containerId).innerHTML = items.map(o => `
    <div class="produto-card">
      <h4>${o.nome}</h4>
      <p>${o.sabor ?? o.tipo}</p>
      <p>R$ ${(o.preco ?? o.valorMensal).toFixed(2)}</p>
    </div>
  `).join('');
}

// inicializações
window.addEventListener('DOMContentLoaded', ()=>{
  refreshLista('/doces/listar','listaDoces');
  refreshLista('/planos/listar','listaPlanos');
  addItem(); // um item inicial no cadastro
});
