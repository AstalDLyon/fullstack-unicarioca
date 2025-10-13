'use strict';

(function init() {
  const novoAlunoForm = document.getElementById('novoAlunoForm');
  const msgAluno = document.getElementById('msgAluno');
  const atribuirForm = document.getElementById('atribuirForm');
  const msgAtribuir = document.getElementById('msgAtribuir');

  const selAluno = document.getElementById('selAluno');
  const selTreino = document.getElementById('selTreino');
  const btnExcluirAluno = document.getElementById('btnExcluirAluno');

  function showMsg(el, text, ok = false) {
    el.textContent = text;
    el.style.display = 'block';
    el.style.color = ok ? 'green' : 'crimson';
  }

  async function carregarAlunos() {
    try {
      const r = await fetch('http://localhost:8080/alunos');
      if (!r.ok) throw new Error('Falha ao carregar alunos');
      const data = await r.json();
      selAluno.innerHTML = '<option value="">-- Selecione --</option>';
      data.forEach(a => {
        const opt = document.createElement('option');
        opt.value = a.id;
        opt.textContent = `${a.id} - ${a.nome} (${a.email})`;
        selAluno.appendChild(opt);
      });
      // desabilita o botão se nada estiver selecionado
      btnExcluirAluno.disabled = !selAluno.value;
    } catch (e) {
      console.error(e);
      showMsg(msgAtribuir, 'Não foi possível carregar os alunos');
    }
  }

  async function carregarTreinos() {
    try {
      const r = await fetch('http://localhost:8080/api/treinos');
      if (!r.ok) throw new Error('Falha ao carregar treinos');
      const data = await r.json();
      selTreino.innerHTML = '<option value="">-- Selecione --</option>';
      data.forEach(t => {
        const opt = document.createElement('option');
        opt.value = t.id;
        opt.textContent = `${t.id} - ${t.nome}`;
        selTreino.appendChild(opt);
      });
    } catch (e) {
      console.error(e);
      showMsg(msgAtribuir, 'Não foi possível carregar os treinos');
    }
  }

  novoAlunoForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    msgAluno.style.display = 'none';
    const nome = document.getElementById('alunoNome').value.trim();
    const email = document.getElementById('alunoEmail').value.trim();
    const senha = document.getElementById('alunoSenha').value;

    if (!nome || !email || !senha) {
      showMsg(msgAluno, 'Preencha todos os campos');
      return;
    }

    try {
      const r = await fetch('http://localhost:8080/alunos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome, email, senha, instrutor: null })
      });
      if (!r.ok) throw new Error(await r.text());
      const novo = await r.json();
      showMsg(msgAluno, `Aluno cadastrado (ID ${novo.id})`, true);
      novoAlunoForm.reset();
      await carregarAlunos();
    } catch (e) {
      console.error(e);
      showMsg(msgAluno, 'Erro ao cadastrar aluno');
    }
  });

  atribuirForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    msgAtribuir.style.display = 'none';
    const alunoId = selAluno.value;
    const treinoId = selTreino.value;
    if (!alunoId || !treinoId) {
      showMsg(msgAtribuir, 'Selecione aluno e treino');
      return;
    }

    try {
      const r = await fetch(`http://localhost:8080/api/treinos/${treinoId}/atribuir/${alunoId}`, {
        method: 'POST'
      });
      if (!r.ok) throw new Error(await r.text());
      showMsg(msgAtribuir, 'Treino atribuído ao aluno', true);
    } catch (e) {
      console.error(e);
      showMsg(msgAtribuir, 'Erro ao atribuir treino');
    }
  });

  // Habilita/desabilita o botão Excluir conforme seleção
  selAluno.addEventListener('change', () => {
    btnExcluirAluno.disabled = !selAluno.value;
  });

  // Excluir aluno selecionado
  btnExcluirAluno.addEventListener('click', async () => {
    const alunoId = selAluno.value;
    if (!alunoId) return;
    const nomeSelecionado = selAluno.options[selAluno.selectedIndex]?.textContent || 'o aluno';
    const confirmar = window.confirm(`Tem certeza que deseja excluir ${nomeSelecionado}?\nIsso removerá também seus treinos e medidas.`);
    if (!confirmar) return;

    try {
      const r = await fetch(`http://localhost:8080/alunos/${alunoId}`, { method: 'DELETE' });
      if (!r.ok && r.status !== 204) throw new Error(await r.text());
      showMsg(msgAtribuir, 'Aluno excluído com sucesso', true);
      await carregarAlunos();
      // Limpa seleção de treino por segurança
      selTreino.value = '';
    } catch (e) {
      console.error(e);
      showMsg(msgAtribuir, 'Erro ao excluir aluno');
    }
  });

  // init
  carregarAlunos();
  carregarTreinos();
})();
