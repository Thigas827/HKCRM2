// app.js

// --- Configurações iniciais ---
const API_BASE = 'http://localhost:8080'; // ajuste se necessário

// --- Navegação entre seções ---
document.querySelectorAll('nav a').forEach(link => {
  link.addEventListener('click', () => {
    document.querySelectorAll('section').forEach(s => s.classList.remove('active'));
    document.getElementById(link.dataset.target).classList.add('active');
  });
});

// --- Alternar tema claro/escuro ---
const themeBtn = document.getElementById('toggleTheme');
let darkMode = false;
themeBtn.addEventListener('click', () => setTheme(!darkMode));

function setTheme(dark) {
  darkMode = dark;
  document.body.classList.toggle('dark', dark);
  document.querySelectorAll('section, nav, h2, input, select, button, table, th, td, .compra-card')
    .forEach(el => el.classList.toggle('dark', dark));
  themeBtn.textContent = dark ? 'Modo Claro' : 'Modo Escuro';
}

// --- Login (fachada) ---
const formLogin = document.getElementById('formLogin');
formLogin.addEventListener('submit', e => {
  e.preventDefault();
  document.getElementById('loginMsg').textContent = '✔️ Login realizado!';
  // exibe aba de cadastro
  document.querySelectorAll('section').forEach(s => s.classList.remove('active'));
  document.getElementById('cadastro').classList.add('active');
});

// --- Cadastro de Cliente + Compra ---
const formCadastro = document.getElementById('formCadastro');
formCadastro.addEventListener('submit', async e => {
  e.preventDefault();
  const dtoCliente = {
    nome:     document.getElementById('nome').value.trim(),
    email:    document.getElementById('email').value.trim(),
    endereco: document.getElementById('endereco').value.trim(),
    telefone: document.getElementById('telefone').value.trim(),
    senha:    '123456'  // senha padrão
  };

  // cria cliente
  let resp = await fetch(`${API_BASE}/usuarios/criar`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(dtoCliente)
  });
  if (!resp.ok) {
    document.getElementById('cadastroMsg').textContent = '❌ Erro ao cadastrar cliente.';
    return;
  }
  const cliente = await resp.json();

  // monta itens
  const selects  = [...document.querySelectorAll('#itensCompraSelects select')];
  const inputsQtd = [...document.querySelectorAll('#itensCompraSelects input[type="number"]')];
  const itens = selects.map((sel, i) => ({
    produtoId: +sel.value,
    quantidade: +inputsQtd[i].value
  })).filter(it => it.quantidade > 0);

  // registra compra (se houver itens)
  if (itens.length) {
    resp = await fetch(`${API_BASE}/compras`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({ clienteId: cliente.id, itens })
    });
    if (!resp.ok) {
      document.getElementById('cadastroMsg').textContent = '✔️ Cliente criado, mas ❌ erro ao registrar compra.';
      return;
    }
    document.getElementById('cadastroMsg').textContent = '✅ Cliente e compra cadastrados com sucesso!';
  } else {
    document.getElementById('cadastroMsg').textContent = '✅ Cliente cadastrado (sem compra).';
  }

  formCadastro.reset();
  // recarrega selects
  window.dispatchEvent(new Event('recarregarSelects'));
});

// --- Histórico de Compras ---
const formHistorico = document.getElementById('formHistorico');
formHistorico.addEventListener('submit', async e => {
  e.preventDefault();
  const clienteId = document.getElementById('clienteSelect').value;
  if (!clienteId) return;

  const resp = await fetch(`${API_BASE}/compras/detalhes?clienteId=${clienteId}`);
  if (!resp.ok) {
    document.getElementById('historicoMsg').textContent = '❌ Erro ao buscar histórico.';
    return;
  }
  const compras = await resp.json();
  const container = document.getElementById('historicoCompras');
  container.innerHTML = '';

  if (!compras.length) {
    document.getElementById('historicoMsg').textContent = 'Nenhuma compra encontrada.';
    return;
  }
  document.getElementById('historicoMsg').textContent = '';

  compras.forEach(c => {
    const card = document.createElement('div');
    card.className = 'compra-card';
    card.innerHTML = `
      <div>
        <strong>Compra #${c.id}</strong><br>
        Data: ${new Date(c.dataCompra).toLocaleString()}<br>
        Valor Total: R$ ${c.valorTotal.toFixed(2)}
      </div>
      <table>
        <thead><tr><th>Produto</th><th>Qtd</th><th>Unit.</th><th>Subtotal</th></tr></thead>
        <tbody>
          ${c.itens.map(i=>`
            <tr>
              <td>${i.nomeProduto}</td>
              <td>${i.quantidade}</td>
              <td>R$ ${i.precoUnitario.toFixed(2)}</td>
              <td>R$ ${i.subtotal.toFixed(2)}</td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    `;
    if (darkMode) card.classList.add('dark');
    container.appendChild(card);
  });
});

// --- Preenche select de clientes ao abrir 'Histórico' ---
document.querySelector('a[data-target="historico"]').addEventListener('click', async () => {
  const sel = document.getElementById('clienteSelect');
  sel.innerHTML = '<option>Carregando...</option>';
  const resp = await fetch(`${API_BASE}/usuarios/todos`);
  const clientes = resp.ok ? await resp.json() : [];
  sel.innerHTML = clientes.length
    ? clientes.map(c=>`<option value="${c.id}">${c.nome} (${c.email})</option>`).join('')
    : '<option>Nenhum cliente</option>';
});

// --- Inicia tema padrão ---
document.addEventListener('DOMContentLoaded', () => setTheme(false));
