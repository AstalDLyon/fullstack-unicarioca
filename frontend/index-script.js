/**
 * index-script.js
 * 
 * Este arquivo é responsável por gerenciar o comportamento da página inicial do sistema UniGym.
 * Ele adapta a interface com base no estado de autenticação do usuário, mostrando opções
 * personalizadas para usuários logados e opções padrão para visitantes.
 * 
 * Funcionalidades principais:
 * - Verificação do estado de autenticação
 * - Personalização do menu de navegação para usuários logados
 * - Adição de opções de acesso rápido (Meus Treinos, Minhas Medidas)
 * - Funcionalidade de logout
 * - Smooth scrolling para links de ancoragem na página
 * 
 * Dependências:
 * - Requer localStorage com 'usuarioId' e 'usuarioNome' para usuários autenticados
 * - Elementos DOM: nav, .login-btn
 */

document.addEventListener('DOMContentLoaded', function() {
    // Verificar se o usuário está logado
    const usuarioId = localStorage.getItem('usuarioId');
    const usuarioNome = localStorage.getItem('usuarioNome');
    
    if (usuarioId && usuarioNome) {
        // Usuário está logado - modificar o menu
        const nav = document.querySelector('nav ul');
        
        // Remover o botão de login
        const loginBtn = document.querySelector('.login-btn');
        if (loginBtn) {
            loginBtn.parentElement.remove();
        }
        
        // Adicionar área do aluno logado com opções
        const userElement = document.createElement('li');
        userElement.className = 'user-info';
        userElement.innerHTML = `
            <span>Olá, ${usuarioNome}</span>
            <div class="user-menu">
                <a href="meus-treinos.html" class="user-menu-item">Meus Treinos</a>
                <a href="minhas-medidas.html" class="user-menu-item">Minhas Medidas</a>
                <button id="logout-btn">Sair</button>
            </div>
        `;
        nav.appendChild(userElement);
        
        // Adicionar funcionalidade ao botão de logout
        document.getElementById('logout-btn').addEventListener('click', function() {
            localStorage.removeItem('usuarioId');
            localStorage.removeItem('usuarioNome');
            window.location.reload();
        });
    }
});