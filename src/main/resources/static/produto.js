// produto.js

const API_BASE = 'http://localhost:8080'; // mesma base do app.js

// --- Cadastro de Produto ---
const formProduto = document.getElementById('formCadastroProduto');
const msgProduto  = document.getElementById('cadastroProdutoMsg');

formProduto.addEventListener('submit', async e => {
  e.preventDefault();
  const nome  = document.getElementById('nomeProduto').value.trim();
  const preco = parseFloat(document.getElementById('precoProduto').value);
  try {
    const resp = await fetch(`${API_BASE}/produtos`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify({ nome, preco })
    });
    if (!resp.ok) throw new Error(await resp.text());
    msgProduto.textContent = '✅ Produto cadastrado com sucesso!';
    formProduto.reset();
    // recarrega selects se estiver na aba de cadastro
    window.dispatchEvent(new Event('recarregarSelects'));
  } catch (err) {
    msgProduto.textContent = '❌ ' + err.message;
  }
});

// --- Busca de Produto ---
const formBusca = document.getElementById('formBuscaDoce');
const tbl       = document.getElementById('tblProdutos');
const tbody     = tbl.querySelector('tbody');
const msgBusca  = document.getElementById('buscaProdutoMsg');

formBusca.addEventListener('submit', async e => {
  e.preventDefault();
  const termo = document.getElementById('buscaNomeProduto').value.trim().toLowerCase();
  try {
    const resp = await fetch(`${API_BASE}/produtos`);
    if (!resp.ok) throw new Error(`Status ${resp.status}`);
    const prods = await resp.json();
    tbody.innerHTML = '';
    let cont = 0;
    prods.forEach(p => {
      if (!termo || p.nome.toLowerCase().includes(termo)) {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${p.id}</td>
          <td>${p.nome}</td>
          <td>R$ ${p.preco.toFixed(2)}</td>
        `;
        tbody.appendChild(tr);
        cont++;
      }
    });
    tbl.classList.toggle('hidden', cont === 0);
    msgBusca.textContent = cont ? '' : 'Nenhum produto encontrado.';
  } catch (err) {
    msgBusca.textContent = '❌ Erro ao buscar.';
  }
});

// --- Selects dinâmicos de produtos para a compra ---
let listaProdutos = [];

async function carregarProdutosSelect() {
  try {
    const resp = await fetch(`${API_BASE}/produtos`);
    listaProdutos = resp.ok ? await resp.json() : [];
  } catch {
    listaProdutos = [];
  }
  atualizarSelects();
}

// cria um bloco <select> + quantidade + remover
function criarSelect() {
  const div = document.createElement('div');
  div.style.display = 'flex';
  div.style.gap = '0.5rem';

  const sel = document.createElement('select');
  listaProdutos.forEach(p=>{
    const opt = new Option(`${p.nome} — R$${p.preco.toFixed(2)}`, p.id);
    sel.add(opt);
  });
  if (!listaProdutos.length) {
    sel.disabled = true;
    sel.add(new Option('— sem produtos —', ''));
  }

  const inpQtd = document.createElement('input');
  inpQtd.type = 'number'; inpQtd.min = '1'; inpQtd.value = '1'; inpQtd.style.width = '60px';

  const btnRm = document.createElement('button');
  btnRm.type = 'button';
  btnRm.textContent = 'Remover';
  btnRm.onclick = () => div.remove();

  div.append(sel, inpQtd, btnRm);
  return div;
}

function atualizarSelects() {
  const container = document.getElementById('itensCompraSelects');
  container.innerHTML = '';
  container.appendChild(criarSelect());
}

// botão "Adicionar Produto"
document.getElementById('addDoceSelect')
  .addEventListener('click', () => {
    document.getElementById('itensCompraSelects').appendChild(criarSelect());
  });

// recarrega selects sempre que um produto novo for cadastrado
window.addEventListener('recarregarSelects', carregarProdutosSelect);

// carregamento inicial
document.addEventListener('DOMContentLoaded', carregarProdutosSelect);
