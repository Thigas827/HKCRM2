// app.js

// --- Configurações iniciais ---
const API_BASE     = 'http://localhost:8080';
let darkMode       = false;
let listaProdutos  = [];

// Pega elementos que faltavam
const telefone     = document.getElementById('telefone');
const formCadastro = document.getElementById('formCadastro');

// --- Máscara de telefone ---
telefone.addEventListener('input', e => {
  let v = e.target.value.replace(/\D/g,'').slice(0,11);
  v = v.replace(/^(\d{2})(\d)/g,'($1) $2').replace(/(\d{5})(\d)/,'$1-$2');
  e.target.value = v;
});

// --- Navegação ---
document.querySelectorAll('nav a').forEach(link => {
  link.addEventListener('click', () => {
    document.querySelectorAll('section').forEach(s => s.classList.remove('active'));
    document.getElementById(link.dataset.target).classList.add('active');
  });
});

// --- Alternar tema ---
const themeBtn = document.getElementById('toggleTheme');
themeBtn.addEventListener('click', () => setTheme(!darkMode));
function setTheme(dark) {
  darkMode = dark;
  document.body.classList.toggle('dark', dark);
  document.querySelectorAll('section, nav, h2, input, select, button, table, th, td, .compra-card')
    .forEach(el => el.classList.toggle('dark', dark));
  themeBtn.textContent = dark ? 'Modo Claro' : 'Modo Escuro';
}

// --- Carrega lista de produtos para os selects e para o resumo ---
async function carregarProdutosSelect() {
  try {
    const resp = await fetch(`${API_BASE}/produtos`);
    if (!resp.ok) throw new Error(`Status ${resp.status}`);
    listaProdutos = await resp.json();
    atualizarSelectsProdutos();
  } catch (err) {
    console.error('Erro ao carregar produtos:', err);
    document.getElementById('cadastroMsg').textContent =
      'Erro ao carregar lista de produtos.';
  }
}

// Cria um bloco <div> com select + input quantidade + botão remover
function criarSelectProduto() {
  const div = document.createElement('div');
  div.style.display = 'flex';
  div.style.gap = '0.5rem';

  const select = document.createElement('select');
  listaProdutos.forEach(p => {
    const opt = document.createElement('option');
    opt.value = p.id;
    opt.textContent = `${p.nome} - R$ ${p.preco.toFixed(2)}`;
    select.appendChild(opt);
  });

  const inputQtd = document.createElement('input');
  inputQtd.type = 'number';
  inputQtd.min = '1';
  inputQtd.value = '1';
  inputQtd.style.width = '60px';

  const btnRemover = document.createElement('button');
  btnRemover.type = 'button';
  btnRemover.textContent = 'Remover';
  btnRemover.onclick = () => {
    div.remove();
    updateSummary();
  };

  div.append(select, inputQtd, btnRemover);

  // listeners de resumo
  select.addEventListener('change', updateSummary);
  inputQtd.addEventListener('input', updateSummary);

  return div;
}

// Atualiza o container de selects
function atualizarSelectsProdutos() {
  const container = document.getElementById('itensCompraSelects');
  container.innerHTML = '';
  container.appendChild(criarSelectProduto());
  updateSummary();
}

// Resumo de compra (lista + total)
function updateSummary() {
  const summaryList    = document.getElementById('summaryList');
  const summaryTotal   = document.getElementById('summaryTotal');
  const purchaseSummary= document.getElementById('purchaseSummary');

  let total = 0;
  summaryList.innerHTML = '';

  document.querySelectorAll('#itensCompraSelects > div').forEach(div => {
    const sel = div.querySelector('select');
    const qtd= +div.querySelector('input').value;
    const prod = listaProdutos.find(p => p.id == sel.value);
    if (prod && qtd > 0) {
      const subtotal = prod.preco * qtd;
      total += subtotal;
      const li = document.createElement('li');
      li.textContent = `${prod.nome} × ${qtd} = R$ ${subtotal.toFixed(2)}`;
      summaryList.appendChild(li);
    }
  });

  summaryTotal.textContent = total.toFixed(2);
  purchaseSummary.classList.toggle('hidden', total === 0);
}

// Botão “Adicionar Produto”
document.getElementById('addDoceSelect').onclick = () => {
  const container = document.getElementById('itensCompraSelects');
  container.appendChild(criarSelectProduto());
  updateSummary();
};

// --- Login (fachada) ---
document.getElementById('formLogin').addEventListener('submit', e => {
  e.preventDefault();
  document.getElementById('loginMsg').textContent = '✔️ Login realizado!';
  document.querySelectorAll('section').forEach(s => s.classList.remove('active'));
  document.getElementById('cadastro').classList.add('active');
});

// --- Cadastro de Cliente + Compra ---
formCadastro.addEventListener('submit', async e => {
  e.preventDefault();
  const dtoCliente = {
    nome:     document.getElementById('nome').value.trim(),
    email:    document.getElementById('email').value.trim(),
    endereco: document.getElementById('endereco').value.trim(),
    telefone: document.getElementById('telefone').value.trim(),
    senha:    '123456'
  };

  let resp = await fetch(`${API_BASE}/usuarios/criar`, {
    method: 'POST', headers: {'Content-Type':'application/json'},
    body: JSON.stringify(dtoCliente)
  });
  if (!resp.ok) {
    document.getElementById('cadastroMsg').textContent = '❌ Erro ao cadastrar cliente.';
    return;
  }
  const cliente = await resp.json();

  // monta itens
  const selects  = [...document.querySelectorAll('#itensCompraSelects select')];
  const inputsQtd= [...document.querySelectorAll('#itensCompraSelects input')];
  const itens = selects.map((sel,i)=>({
    produtoId: +sel.value,
    quantidade:+inputsQtd[i].value
  })).filter(it=>it.quantidade>0);

  if (itens.length) {
    resp = await fetch(`${API_BASE}/compras`, {
      method:'POST',headers:{'Content-Type':'application/json'},
      body: JSON.stringify({ clienteId:cliente.id, itens })
    });
    if (resp.ok) {
      document.getElementById('cadastroMsg').textContent =
        '✅ Cliente e compra cadastrados com sucesso!';
    } else {
      document.getElementById('cadastroMsg').textContent =
        '✔️ Cliente criado, mas ❌ erro ao registrar compra.';
    }
  } else {
    document.getElementById('cadastroMsg').textContent =
      '✅ Cliente cadastrado (sem compra).';
  }

  e.target.reset();
  carregarProdutosSelect();
});

// --- Histórico de Cliente + Compras ---
document.getElementById('formHistorico').addEventListener('submit', async e => {
  e.preventDefault();
  const id = document.getElementById('clienteSelect').value;
  if (!id) return;

  const msgEl    = document.getElementById('historicoMsg');
  const container= document.getElementById('historicoCompras');
  msgEl.textContent = '';
  container.innerHTML = '';

  try {
    const resp = await fetch(`${API_BASE}/compras/historico?clienteId=${id}`);
    if (!resp.ok) throw new Error(resp.status);
    const { cliente, compras } = await resp.json();

    // Dados do cliente
    const cd = document.createElement('div');
    cd.innerHTML = `
      <h3>Cliente</h3>
      <p><strong>Nome:</strong> ${cliente.nome}</p>
      <p><strong>E-mail:</strong> ${cliente.email}</p>
      <p><strong>Telefone:</strong> ${cliente.telefone}</p>
      <p><strong>Endereço:</strong> ${cliente.endereco}</p>
      <hr>
    `;
    container.appendChild(cd);

    if (!compras.length) {
      msgEl.textContent = 'Nenhuma compra encontrada.';
      return;
    }

    compras.forEach(c => {
      const divC = document.createElement('div');
      divC.className = 'compra-card';
      divC.innerHTML = `
        <div class="compra-header">
          <strong>Compra #${c.id}</strong>
          <span>${new Date(c.dataCompra).toLocaleString()}</span>
          <span>R$ ${c.valorTotal.toFixed(2)}</span>
        </div>
      `;
      const tbl = document.createElement('table');
      tbl.innerHTML = `
        <thead><tr>
          <th>Produto</th><th>Qtd</th><th>Preço unit.</th><th>Subtotal</th>
        </tr></thead>
        <tbody>
          ${c.itens.map(i=>`
            <tr>
              <td>${i.nomeProduto}</td>
              <td>${i.quantidade}</td>
              <td>R$ ${i.precoUnit.toFixed(2)}</td>
              <td>R$ ${i.subtotal.toFixed(2)}</td>
            </tr>
          `).join('')}
        </tbody>
      `;
      divC.appendChild(tbl);
      container.appendChild(divC);
    });
  } catch (err) {
    console.error(err);
    msgEl.textContent = 'Erro ao carregar histórico.';
  }
});

// --- Preencher select de clientes ao clicar em "Histórico" ---
document.querySelector('a[data-target="historico"]').addEventListener('click', async () => {
  const sel = document.getElementById('clienteSelect');
  sel.innerHTML = '<option>Carregando...</option>';
  const resp = await fetch(`${API_BASE}/usuarios/todos`);
  const clientes = resp.ok ? await resp.json() : [];
  sel.innerHTML = clientes.length
    ? clientes.map(c=>`<option value="${c.id}">${c.nome} (${c.email})</option>`).join('')
    : '<option>Nenhum cliente</option>';
});

// Inicia tudo
document.addEventListener('DOMContentLoaded', () => {
  setTheme(false);
  carregarProdutosSelect();
});
