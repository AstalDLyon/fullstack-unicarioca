// Cadastrar Aluno - integra com POST /alunos e carrega lista de instrutores
(function () {
  const form = document.getElementById('alunoForm');
  const nome = document.getElementById('nome');
  const email = document.getElementById('email');
  const senha = document.getElementById('senha');
  const confirmSenha = document.getElementById('confirmSenha');
  const instrutorSelect = document.getElementById('instrutor');
  const mensagem = document.getElementById('mensagem');

  // Atribuição de treino na página de cadastro
  const atribuirForm = document.getElementById('atribuirFormCadastro');
  const selAlunoCadastro = document.getElementById('selAlunoCadastro');
  const selTreinoCadastro = document.getElementById('selTreinoCadastro');
  const msgAtribuirCadastro = document.getElementById('msgAtribuirCadastro');
  const btnExcluirAlunoCadastro = document.getElementById('btnExcluirAlunoCadastro');
  const msgExcluirCadastro = document.getElementById('msgExcluirCadastro');

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

  async function carregarAlunosCadastro() {
    if (!selAlunoCadastro) return;
    try {
      const resp = await fetch('http://localhost:8080/alunos');
      if (!resp.ok) throw new Error('Falha ao carregar alunos');
      const data = await resp.json();
      selAlunoCadastro.innerHTML = '<option value="">-- Selecione --</option>';
      data.forEach(a => {
        const opt = document.createElement('option');
        opt.value = a.id;
        opt.textContent = `${a.id} - ${a.nome} (${a.email})`;
        selAlunoCadastro.appendChild(opt);
      });
      if (btnExcluirAlunoCadastro) btnExcluirAlunoCadastro.disabled = !selAlunoCadastro.value;
    } catch (err) {
      console.error('Erro ao carregar alunos:', err);
      if (msgAtribuirCadastro) {
        msgAtribuirCadastro.textContent = 'Não foi possível carregar os alunos';
        msgAtribuirCadastro.style.display = 'block';
        msgAtribuirCadastro.style.color = 'crimson';
      }
    }
  }

  async function carregarTreinosCadastro() {
    if (!selTreinoCadastro) return;
    try {
      const resp = await fetch('http://localhost:8080/api/treinos');
      if (!resp.ok) throw new Error('Falha ao carregar treinos');
      const data = await resp.json();
      selTreinoCadastro.innerHTML = '<option value="">-- Selecione --</option>';
      data.forEach(t => {
        const opt = document.createElement('option');
        opt.value = t.id;
        opt.textContent = `${t.id} - ${t.nome}`;
        selTreinoCadastro.appendChild(opt);
      });
    } catch (err) {
      console.error('Erro ao carregar treinos:', err);
      if (msgAtribuirCadastro) {
        msgAtribuirCadastro.textContent = 'Não foi possível carregar os treinos';
        msgAtribuirCadastro.style.display = 'block';
        msgAtribuirCadastro.style.color = 'crimson';
      }
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
      // Atualiza lista de alunos na box de atribuição
      await carregarAlunosCadastro();
    } catch (err) {
      console.error('Erro ao cadastrar aluno:', err);
      setMessage('Erro ao cadastrar aluno. Verifique os dados e tente novamente.');
    }
  });

  // init
  carregarInstrutores();
  carregarAlunosCadastro();
  carregarTreinosCadastro();

  if (atribuirForm) {
    atribuirForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      if (!msgAtribuirCadastro) return;
      msgAtribuirCadastro.style.display = 'none';
      const alunoId = selAlunoCadastro.value;
      const treinoId = selTreinoCadastro.value;
      if (!alunoId || !treinoId) {
        msgAtribuirCadastro.textContent = 'Selecione aluno e treino';
        msgAtribuirCadastro.style.display = 'block';
        msgAtribuirCadastro.style.color = 'crimson';
        return;
      }

      try {
        const r = await fetch(`http://localhost:8080/api/treinos/${treinoId}/atribuir/${alunoId}`, { method: 'POST' });
        if (!r.ok) throw new Error(await r.text());
        msgAtribuirCadastro.textContent = 'Treino atribuído ao aluno';
        msgAtribuirCadastro.style.display = 'block';
        msgAtribuirCadastro.style.color = 'green';
      } catch (err) {
        console.error(err);
        msgAtribuirCadastro.textContent = 'Erro ao atribuir treino';
        msgAtribuirCadastro.style.display = 'block';
        msgAtribuirCadastro.style.color = 'crimson';
      }
    });
  }

  // Habilita/desabilita excluir conforme seleção
  if (selAlunoCadastro && btnExcluirAlunoCadastro) {
    selAlunoCadastro.addEventListener('change', () => {
      btnExcluirAlunoCadastro.disabled = !selAlunoCadastro.value;
    });

    btnExcluirAlunoCadastro.addEventListener('click', async () => {
      if (!selAlunoCadastro.value) return;
      if (msgExcluirCadastro) msgExcluirCadastro.style.display = 'none';
      const label = selAlunoCadastro.options[selAlunoCadastro.selectedIndex]?.textContent || 'o aluno';
      const confirmar = window.confirm(`Tem certeza que deseja excluir ${label}?\nIsso removerá também seus treinos e medidas.`);
      if (!confirmar) return;
      try {
        const r = await fetch(`http://localhost:8080/alunos/${selAlunoCadastro.value}`, { method: 'DELETE' });
        if (!r.ok && r.status !== 204) throw new Error(await r.text());
        if (msgExcluirCadastro) {
          msgExcluirCadastro.textContent = 'Aluno excluído com sucesso';
          msgExcluirCadastro.style.display = 'block';
          msgExcluirCadastro.style.color = 'green';
        }
        await carregarAlunosCadastro();
        selTreinoCadastro.value = '';
      } catch (err) {
        console.error(err);
        if (msgExcluirCadastro) {
          msgExcluirCadastro.textContent = 'Erro ao excluir aluno';
          msgExcluirCadastro.style.display = 'block';
          msgExcluirCadastro.style.color = 'crimson';
        }
      }
    });
  }
})();
