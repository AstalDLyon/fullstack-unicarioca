/**
 * auth-script.js
 * 
 * Este arquivo é responsável pela autenticação de usuários no sistema UniGym.
 * Ele gerencia o processo de login, validação de credenciais e armazenamento
 * da sessão do usuário no localStorage.
 * 
 * Funcionalidades principais:
 * - Processamento do formulário de login
 * - Envio de requisições de autenticação para o backend
 * - Armazenamento de dados do usuário autenticado
 * - Redirecionamento após login bem-sucedido
 * - Exibição de mensagens de erro para o usuário
 * 
 * Dependências:
 * - Requer um elemento HTML com id 'loginForm'
 * - Requer campos de formulário com ids 'email' e 'senha'
 * - Requer um elemento HTML com id 'mensagem' para exibir feedback
 */

document.addEventListener('DOMContentLoaded', function() {
    // Setup do formulário de login
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const senha = document.getElementById('senha').value;
            
            // Dados a serem enviados para o back-end
            const dados = {
                email: email,
                senha: senha
            };
            
            // URL do endpoint de autenticação
            const url = 'http://localhost:8080/api/auth/login';
            
            // Configuração da requisição
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            })
            .then(response => response.json())
                        .then(data => {
                const mensagemElement = document.getElementById('mensagem');
                mensagemElement.style.display = 'block';
                
                if (data.success) {
                    // Login bem-sucedido
                                        mensagemElement.textContent = 'Login realizado com sucesso! Redirecionando...';
                    mensagemElement.style.color = 'green';
                    
                    // Armazenar informações do usuário no localStorage
                    localStorage.setItem('usuarioId', data.id);
                    localStorage.setItem('usuarioNome', data.nome);
                                        if (data.role) localStorage.setItem('usuarioRole', data.role);
                    
                    // Redirecionar para a página principal após um breve delay
                    setTimeout(() => {
                                                if (data.redirect) {
                                                    window.location.href = data.redirect.replace(/^\//, '');
                                                } else if (data.role === 'INSTRUTOR') {
                                                    window.location.href = 'cadastrar-aluno.html';
                                                } else {
                                                    window.location.href = 'meus-treinos.html';
                                                }
                    }, 1500);
                } else {
                    // Login falhou
                    mensagemElement.textContent = data.message || 'Falha na autenticação. Verifique suas credenciais.';
                    mensagemElement.style.color = 'red';
                }
            })
            .catch(error => {
                console.error('Erro ao processar login:', error);
                const mensagemElement = document.getElementById('mensagem');
                mensagemElement.style.display = 'block';
                mensagemElement.textContent = 'Erro ao conectar com o servidor. Tente novamente mais tarde.';
                mensagemElement.style.color = 'red';
            });
        });
    }
});