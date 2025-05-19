// URL base do seu backend
const API = 'http://localhost:8080';

let authHeader = null;

// Navegação entre seções
document.querySelectorAll('nav a').forEach(a=>{
  a.onclick = e => {
    document.querySelectorAll('section').forEach(s=>s.classList.remove('active'));
    document.getElementById(a.dataset.target).classList.add('active');
  };
});

// Modo escuro
const themeBtn = document.getElementById('toggleTheme');
themeBtn.onclick = ()=>{
  document.body.classList.toggle('dark');
  themeBtn.textContent = document.body.classList.contains('dark') ? 'Modo Claro' : 'Modo Escuro';
};

// ------------ LOGIN ------------
document.getElementById('formLogin').onsubmit = async e=>{
  e.preventDefault();
  const email = loginEmail.value, senha = loginSenha.value;
  const resp = await fetch(`${API}/usuarios/login`, {
    method:'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({email,senha})
  });
  if(resp.ok){
    authHeader = btoa(`${email}:${senha}`);
    loginMsg.textContent = '✔️ Logado!';
  } else {
    loginMsg.textContent = '❌ E-mail ou senha inválidos';
  }
};

// ------------ CADASTRO CLIENTE + COMPRA ------------
document.getElementById('addItem').onclick = addItem;
function addItem(){
  const div = document.createElement('div');
  div.innerHTML = `
    <select class="prodSel"></select>
    <input type="number" class="qtde" value="1" min="1">
    <button class="rem">X</button>`;
  div.querySelector('.rem').onclick = ()=>div.remove();
  document.getElementById('itensCompra').append(div);
  loadProdutos(div.querySelector('.prodSel'));
}
async function loadProdutos(sel){
  const r=await fetch(`${API}/produtos`);
  const ps=await r.json();
  sel.innerHTML = ps.map(p=>`<option value="${p.id}">${p.nome} (R$${p.preco.toFixed(2)})</option>`).join('');
}
document.getElementById('formCadastro').onsubmit = async e=>{
  e.preventDefault();
  const cliente = {
    nome: cadNome.value,
    email: cadEmail.value,
    senha: 'Temp123!', // default
    endereco: cadEndereco.value,
    telefone: cadTelefone.value,
    roles: [{nome:'ROLE_CLIENTE'}]
  };
  // cria usuário
  const cr = await fetch(`${API}/usuarios/criar`, {
    method:'POST',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify(cliente)
  });
  if(!cr.ok){ cadMsg.textContent='❌ Erro ao cadastrar.'; return; }
  const user = await cr.json();
  // registra compra
  const itens = [...document.querySelectorAll('#itensCompra>div')].map(d=>({
    produtoId: +d.querySelector('.prodSel').value,
    quantidade: +d.querySelector('.qtde').value
  }));
  const cp = await fetch(`${API}/compras`, {
    method:'POST',
    headers:{
      'Content-Type':'application/json',
      'Authorization': 'Basic '+btoa(`${user.email}:Temp123!`)
    },
    body: JSON.stringify({clienteId:user.id,itens})
  });
  if(cp.ok){
    cadMsg.textContent=`✅ Venda registrada!`;
    formCadastro.reset(); itensCompra.innerHTML='';
  } else {
    cadMsg.textContent='❌ Erro ao registrar compra.';
  }
};
// ------------ CADASTRO DOCE + PLANO ------------



// ------------ HISTÓRICO ------------
document.getElementById('formHistorico').onsubmit = async e=>{
  e.preventDefault();
  const email = histEmail.value;
  // busca cliente por e-mail
  const ru = await fetch(`${API}/usuarios/buscar?email=${encodeURIComponent(email)}`);
  if(!ru.ok){ histResultado.textContent='Cliente não encontrado'; return; }
  const u = await ru.json();
  // busca compras
  const rc = await fetch(`${API}/compras?clienteId=${u.id}`);
  const list = await rc.json();
  histResultado.innerHTML = list.length
    ? list.map(c=>`<div class="compra-card">
        <div class="compra-header">
          <strong>#${c.id}</strong> ${new Date(c.dataCompra).toLocaleString()}
          <span>R$ ${c.valorTotal.toFixed(2)}</span>
        </div>
      </div>`).join('')
    : 'Nenhuma compra.';
};

// ------------ DOCES & PLANOS ------------
tabButtons = document.querySelectorAll('.tab-button');
tabButtons.forEach(btn=>{
  btn.onclick = ()=>{
    btn.parentNode.querySelectorAll('button').forEach(b=>b.classList.remove('active'));
    btn.classList.add('active');
    btn.parentNode.nextElementSibling.querySelectorAll('.tab-content').forEach(tc=>tc.classList.remove('active'));
    document.getElementById(btn.dataset.tab).classList.add('active');
  };
});
async function refreshLista(url, container){
  const r=await fetch(API+url); const items=await r.json();
  document.getElementById(container).innerHTML =
    items.map(o=>`<div class="produto-card">
      <h4>${o.nome}</h4>
      <p>${o.sabor||o.tipoPlano||''}</p>
      <p>R$ ${ (o.preco||o.valorMensalidade).toFixed(2) }</p>
    </div>`).join('');
}
// cadastro
document.getElementById('formDoce').onsubmit = async e=>{
  e.preventDefault();
  const dto = {
    nome:doceNome.value,
    sabor:doceSabor.value,
    preco:parseFloat(docePreco.value),
    quantidade:parseInt(doceQtde.value)
  };
  await fetch(`${API}/doces/criar`,{
    method:'POST', headers:{'Content-Type':'application/json'},
    body: JSON.stringify(dto)
  });
  refreshLista('/doces','listaDoces');
};
document.getElementById('formPlano').onsubmit = async e=>{
  e.preventDefault();
  const dto = {
    nome:planoNome.value,
    tipoPlano:planoTipo.value,
    valorMensalidade:parseFloat(planoValor.value)
  };
  await fetch(`${API}/planos/criar`,{
    method:'POST', headers:{'Content-Type':'application/json'},
    body: JSON.stringify(dto)
  });
  refreshLista('/planos/listar','listaPlanos');
};
// inicializações
window.addEventListener('DOMContentLoaded',()=>{
  refreshLista('/doces','listaDoces');
  refreshLista('/planos/listar','listaPlanos');
  addItem(); // um item inicial no cadastro
});
