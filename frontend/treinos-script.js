/**
 * treinos-script.js
 * 
 * Este arquivo é responsável por gerenciar a visualização e interação com os treinos
 * dos alunos no sistema UniGym. Ele carrega treinos ativos de um aluno específico
 * e os apresenta na interface de usuário de forma organizada.
 * 
 * Funcionalidades principais:
 * - Verificação de autenticação do usuário
 * - Redirecionamento para login se não autenticado
 * - Carregamento de treinos ativos do backend
 * - Organização dos treinos por dia da semana
 * - Exibição de detalhes de exercícios em cada treino
 * - Funcionalidade de logout
 * 
 * Funções:
 * - carregarTreinos: Busca os treinos ativos do aluno no backend e os exibe na interface
 * - criarCardTreino: Cria elementos HTML para exibir informações de um treino específico
 * - criarCardExercicio: Cria elementos HTML para exibir detalhes de um exercício
 * - getDiaSemanaFormatado: Converte o formato do dia da semana para exibição
 * 
 * Dependências:
 * - Requer localStorage com 'usuarioId' e 'usuarioNome'
 * - API backend: /api/treinos/aluno/{id}/ativos
 */

document.addEventListener('DOMContentLoaded', function() {
    // Verificar se o usuário está logado
    const usuarioId = localStorage.getItem('usuarioId');
    const usuarioNome = localStorage.getItem('usuarioNome');
    
    console.log('ID do usuário recuperado:', usuarioId);
    console.log('Nome do usuário recuperado:', usuarioNome);
    
    if (!usuarioId) {
        // Redirecionar para a página de login se não estiver logado
        window.location.href = 'login.html';
        return;
    }
    
    // Adicionar nome do usuário na barra de navegação
    const nav = document.querySelector('nav ul');
    const userElement = document.createElement('li');
    userElement.className = 'user-info';
    userElement.innerHTML = `
        <span>Olá, ${usuarioNome}</span>
        <button id="logout-btn">Sair</button>
    `;
    nav.appendChild(userElement);
    
    // Adicionar funcionalidade ao botão de logout
    document.getElementById('logout-btn').addEventListener('click', function() {
        localStorage.removeItem('usuarioId');
        localStorage.removeItem('usuarioNome');
        window.location.href = 'index.html';
    });
    
    // Carregar os treinos ativos do usuário
    carregarTreinos(usuarioId);
});

function carregarTreinos(alunoId) {
    console.log('Carregando treinos para o aluno ID:', alunoId);
    
    const treinosContainer = document.getElementById('treinos-container');
    treinosContainer.innerHTML = '<div class="loading">Carregando seus treinos...</div>';
    
    fetch(`http://localhost:8080/api/treinos/aluno/${alunoId}/ativos`)
        .then(response => {
            console.log('Status da resposta:', response.status);
            if (!response.ok) {
                throw new Error(`Falha ao carregar treinos: ${response.status} ${response.statusText}`);
            }
            return response.text().then(text => {
                // Log do texto bruto para depuração
                console.log('Resposta bruta (primeiros 500 caracteres):', text.substring(0, 500));
                try {
                    return JSON.parse(text);
                } catch (e) {
                    console.error('Erro ao parsear JSON:', e);
                    throw new Error('Erro ao processar resposta do servidor');
                }
            });
        })
        .then(treinos => {
            console.log('Treinos recebidos:', treinos);
            const treinosContainer = document.getElementById('treinos-container');
            
            if (treinos.length === 0) {
                treinosContainer.innerHTML = `
                    <div class="sem-treinos">
                        <h3>Você não possui treinos ativos no momento.</h3>
                        <p>Fale com seu instrutor para criar um novo treino.</p>
                    </div>
                `;
                return;
            }
            
            // Construir a exibição dos treinos
            let html = '';
            
            treinos.forEach(treino => {
                const dataInicio = new Date(treino.dataInicio).toLocaleDateString('pt-BR');
                const dataFim = treino.dataFim ? new Date(treino.dataFim).toLocaleDateString('pt-BR') : 'Em andamento';
                
                html += `
                    <div class="treino-container" data-treino-id="${treino.id}">
                        <div class="treino-header">
                            <h3 class="treino-title">${treino.nome}</h3>
                            <span class="treino-date">${dataInicio} até ${dataFim}</span>
                        </div>
                        
                        <p class="treino-description">${treino.descricao || 'Sem descrição'}</p>
                        
                        <div class="dias-treino" id="dias-treino-${treino.id}">
                            <div class="loading">Carregando dias de treino...</div>
                        </div>
                        
                        <div id="exercicios-${treino.id}" class="exercicios-container">
                            <div class="loading">Selecione um dia de treino</div>
                        </div>
                    </div>
                `;
            });
            
            treinosContainer.innerHTML = html;
            
            // Carregar os dias de treino para cada treino
            treinos.forEach(treino => {
                carregarDiasTreino(treino.id);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar treinos:', error);
            document.getElementById('treinos-container').innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar seus treinos. Por favor, tente novamente mais tarde.</p>
                </div>
            `;
        });
}

function carregarDiasTreino(treinoId) {
    fetch(`http://localhost:8080/api/exercicios/treino/${treinoId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Falha ao carregar exercícios');
            }
            return response.json();
        })
        .then(exercicios => {
            // Extrair dias únicos de treino
            const diasUnicos = [...new Set(exercicios.map(exercicio => exercicio.diaSemana))];
            
            if (diasUnicos.length === 0) {
                document.getElementById(`dias-treino-${treinoId}`).innerHTML = `
                    <p>Nenhum exercício cadastrado para este treino.</p>
                `;
                return;
            }
            
            // Ordenar os dias da semana
            const ordemDias = {
                'Segunda': 1, 
                'Terça': 2, 
                'Quarta': 3, 
                'Quinta': 4, 
                'Sexta': 5, 
                'Sábado': 6, 
                'Domingo': 7
            };
            
            diasUnicos.sort((a, b) => ordemDias[a] - ordemDias[b]);
            
            // Construir os botões para os dias de treino
            let html = '';
            diasUnicos.forEach((dia, index) => {
                html += `
                    <button class="dia-btn ${index === 0 ? 'active' : ''}" 
                            data-treino-id="${treinoId}" 
                            data-dia="${dia}">
                        ${dia}
                    </button>
                `;
            });
            
            const diaContainer = document.getElementById(`dias-treino-${treinoId}`);
            diaContainer.innerHTML = html;
            
            // Adicionar listeners aos botões
            const botoes = diaContainer.querySelectorAll('.dia-btn');
            botoes.forEach(botao => {
                botao.addEventListener('click', function() {
                    // Remover classe ativa de todos os botões deste treino
                    botoes.forEach(b => b.classList.remove('active'));
                    
                    // Adicionar classe ativa ao botão clicado
                    this.classList.add('active');
                    
                    // Carregar exercícios para o dia selecionado
                    const dia = this.dataset.dia;
                    carregarExerciciosPorDia(treinoId, dia);
                });
            });
            
            // Carregar exercícios para o primeiro dia (selecionado por padrão)
            if (diasUnicos.length > 0) {
                carregarExerciciosPorDia(treinoId, diasUnicos[0]);
            }
        })
        .catch(error => {
            console.error('Erro ao carregar dias de treino:', error);
            document.getElementById(`dias-treino-${treinoId}`).innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar os dias de treino.</p>
                </div>
            `;
        });
}

function carregarExerciciosPorDia(treinoId, diaSemana) {
    fetch(`http://localhost:8080/api/exercicios/treino/${treinoId}?diaSemana=${diaSemana}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Falha ao carregar exercícios');
            }
            return response.json();
        })
        .then(exercicios => {
            const exerciciosContainer = document.getElementById(`exercicios-${treinoId}`);
            
            if (exercicios.length === 0) {
                exerciciosContainer.innerHTML = `
                    <p>Nenhum exercício cadastrado para ${diaSemana}.</p>
                `;
                return;
            }
            
            // Ordenar exercícios por ordem
            exercicios.sort((a, b) => a.ordem - b.ordem);
            
            // Construir a lista de exercícios
            let html = '<ul class="exercicios-lista">';
            
            exercicios.forEach(exercicio => {
                html += `
                    <li class="exercicio-item">
                        <div class="exercicio-header">
                            <span class="exercicio-nome">${exercicio.nome}</span>
                            <span class="exercicio-grupo">${exercicio.grupoMuscular}</span>
                        </div>
                        
                        <div class="exercicio-detalhes">
                            <div class="exercicio-detalhe">
                                <div class="detalhe-label">Séries</div>
                                <div class="detalhe-valor">${exercicio.series}</div>
                            </div>
                            <div class="exercicio-detalhe">
                                <div class="detalhe-label">Repetições</div>
                                <div class="detalhe-valor">${exercicio.repeticoes}</div>
                            </div>
                            <div class="exercicio-detalhe">
                                <div class="detalhe-label">Descanso</div>
                                <div class="detalhe-valor">${exercicio.descanso}s</div>
                            </div>
                            <div class="exercicio-detalhe">
                                <div class="detalhe-label">Carga</div>
                                <div class="detalhe-valor">${exercicio.carga || '-'}</div>
                            </div>
                        </div>
                        
                        ${exercicio.observacoes ? `
                            <div class="exercicio-obs">
                                <strong>Observações:</strong> ${exercicio.observacoes}
                            </div>
                        ` : ''}
                    </li>
                `;
            });
            
            html += '</ul>';
            exerciciosContainer.innerHTML = html;
        })
        .catch(error => {
            console.error('Erro ao carregar exercícios:', error);
            document.getElementById(`exercicios-${treinoId}`).innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar os exercícios.</p>
                </div>
            `;
        });
}