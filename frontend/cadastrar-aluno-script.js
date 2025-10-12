// Cadastrar Aluno - integra com POST /alunos e carrega lista de instrutores
(function () {
  const form = document.getElementById('alunoForm');
  const nome = document.getElementById('nome');
  const email = document.getElementById('email');
  const senha = document.getElementById('senha');
  const confirmSenha = document.getElementById('confirmSenha');
  const instrutorSelect = document.getElementById('instrutor');
  const mensagem = document.getElementById('mensagem');

  // Não exigimos login para cadastrar aluno (pedido do usuário)
  function requireLogin() {
    return true;
  }

  function setMessage(text, ok = false) {
    mensagem.textContent = text;
    mensagem.style.display = 'block';
    mensagem.style.color = ok ? 'green' : 'crimson';
  }

  async function carregarInstrutores() {
    try {
  const resp = await fetch('http://localhost:8080/instrutores');
  if (!resp.ok) throw new Error('Falha ao carregar instrutores');
      const data = await resp.json();
      // Preenche o select com id e nome
      data.forEach((i) => {
        const opt = document.createElement('option');
        opt.value = i.id;
        opt.textContent = `${i.id} - ${i.nome}`;
        instrutorSelect.appendChild(opt);
      });
    } catch (err) {
      console.warn('Instrutores não carregados, seguindo sem seleção:', err);
      // Mantém a opção "Sem instrutor" e segue sem bloquear o cadastro
    }
  }

  function validar() {
    if (!nome.value.trim()) return setMessage('Informe o nome.'), false;
    if (!email.value.trim()) return setMessage('Informe o email.'), false;
    if (!senha.value) return setMessage('Informe a senha.'), false;
    if (senha.value !== confirmSenha.value) return setMessage('As senhas não conferem.'), false;
    return true;
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    mensagem.style.display = 'none';

    // Login não é obrigatório; segue cadastro aberto

    if (!validar()) return;

    const instrutorId = instrutorSelect.value ? Number(instrutorSelect.value) : null;

    const payload = {
      nome: nome.value.trim(),
      email: email.value.trim(),
      senha: senha.value,
      instrutor: instrutorId ? { id: instrutorId } : null
    };

    try {
  const resp = await fetch('http://localhost:8080/alunos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!resp.ok) {
        const txt = await resp.text();
        throw new Error(txt || 'Falha ao cadastrar aluno');
      }

      const novo = await resp.json();
      setMessage(`Aluno cadastrado com sucesso (ID ${novo.id}).`, true);
      form.reset();
      instrutorSelect.value = '';
    } catch (err) {
      console.error('Erro ao cadastrar aluno:', err);
      setMessage('Erro ao cadastrar aluno. Verifique os dados e tente novamente.');
    }
  });

  // init
  carregarInstrutores();
})();
