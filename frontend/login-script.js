/**
 * Script para gerenciar o estado de usuário entre páginas
 */

// Verifica se o usuário está logado ao carregar qualquer página
document.addEventListener('DOMContentLoaded', function() {
    const usuarioId = localStorage.getItem('usuarioId');
    const usuarioNome = localStorage.getItem('usuarioNome');
    
    // Exibir informações do usuário caso esteja logado
    if (usuarioId && usuarioNome) {
        // Verifica se estamos na página index.html
        if (window.location.pathname.includes('index.html') || window.location.pathname.endsWith('/')) {
            // Adiciona elemento de boas-vindas na barra de navegação
            const nav = document.querySelector('nav ul');
            
            if (nav) {
                const userElement = document.createElement('li');
                userElement.className = 'user-info';
                userElement.innerHTML = `
                    <span>Olá, ${usuarioNome}</span>
                    <button id="logout-btn">Sair</button>
                `;
                nav.appendChild(userElement);
                
                // Adiciona funcionalidade ao botão de logout
                document.getElementById('logout-btn').addEventListener('click', function() {
                    localStorage.removeItem('usuarioId');
                    localStorage.removeItem('usuarioNome');
                    window.location.reload();
                });
            }
        }
    } else {
        // Se não estiver logado e estiver tentando acessar uma página protegida
        // Exemplo: se você tiver páginas que precisam de login.
        const paginasProtegidas = ['perfil.html', 'meu-treino.html'];
        const paginaAtual = window.location.pathname.split('/').pop();
        
        if (paginasProtegidas.includes(paginaAtual)) {
            window.location.href = 'login.html';
        }
    }
});

// Funções para consumir a API do backend

/**
 * Busca a lista de todos os alunos
 */
function buscarAlunos() {
    return fetch('http://localhost:8080/alunos')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar alunos');
            }
            return response.json();
        });
}

/**
 * Busca um aluno específico pelo ID, retornando uma Promise.
 * @param {number} id - ID do aluno
 */
function buscarAlunoPorId(id) {
    return fetch(`http://localhost:8080/alunos/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar aluno');
            }
            return response.json();
        });
}

/**
 * Cadastra um novo aluno
 * @param {Object} aluno - Objeto contendo os dados do aluno, não deve conter o ID. tomem cuidado com isso.
 */
function cadastrarAluno(aluno) {
    return fetch('http://localhost:8080/alunos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(aluno)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao cadastrar aluno');
        }
        return response.json();
    });
}