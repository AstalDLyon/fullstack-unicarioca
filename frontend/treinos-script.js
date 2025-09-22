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
    console.log('Carregando dias de treino para o treino ID:', treinoId);
    
    // Elemento onde os dias serão exibidos
    const diaContainer = document.getElementById(`dias-treino-${treinoId}`);
    diaContainer.innerHTML = '<div class="loading">Carregando dias de treino...</div>';
    
    // Mapeamento ordem + label apenas para dias existentes (remove dias artificiais)
    const MAP_ORDER = { SEGUNDA:1, TERCA:2, TERÇA:2, QUARTA:3, QUINTA:4, SEXTA:5 };
    const MAP_EXIBE = { SEGUNDA:'Segunda', TERCA:'Terça', 'TERÇA':'Terça', QUARTA:'Quarta', QUINTA:'Quinta', SEXTA:'Sexta' };
    
    fetch(`http://localhost:8080/api/exercicios/treino/${treinoId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Falha ao carregar exercícios: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(exercicios => {
            console.log('Resposta do servidor para treino ' + treinoId + ':', exercicios);
            
            // Verificação de segurança
            if (!Array.isArray(exercicios)) {
                console.error('Resposta de exercícios não é um array:', exercicios);
                diaContainer.innerHTML = '<p>Erro ao processar dados de exercícios.</p>';
                return;
            }
            
            if (exercicios.length === 0) {
                diaContainer.innerHTML = '<p>Nenhum exercício cadastrado para este treino.</p>';
                return;
            }
            
            // Extrai dias distintos válidos do payload recebido
            const diasValidos = [...new Set(exercicios
                .map(e => (e && e.diaSemana) ? e.diaSemana.toUpperCase() : null)
                .filter(d => d && MAP_ORDER[d] !== undefined))]
                .sort((a,b) => MAP_ORDER[a]-MAP_ORDER[b]);

            console.log('Dias válidos detectados:', diasValidos);

            if (diasValidos.length === 0) {
                diaContainer.innerHTML = '<p>Nenhum dia de treino válido encontrado.</p>';
                return;
            }

            // Gera somente botões dos dias presentes (evita disabled)
            diaContainer.innerHTML = diasValidos.map((dia, idx) => `
                <button class="dia-btn ${idx===0?'active':''}" data-treino-id="${treinoId}" data-dia="${dia}">${MAP_EXIBE[dia]||dia}</button>
            `).join('');

            // Listeners de clique: alterna active e busca exercícios para o dia
            const botoes = diaContainer.querySelectorAll('.dia-btn');
            botoes.forEach(btn => {
                btn.addEventListener('click', () => {
                    botoes.forEach(b=>b.classList.remove('active'));
                    btn.classList.add('active');
                    carregarExerciciosPorDia(treinoId, btn.dataset.dia);
                });
            });

            // Auto-carrega primeiro dia válido
            carregarExerciciosPorDia(treinoId, diasValidos[0]);
        })
        .catch(error => {
            console.error('Erro ao carregar dias de treino:', error);
            diaContainer.innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar os dias de treino: ${error.message}</p>
                </div>
            `;
        });
}

function carregarExerciciosPorDia(treinoId, diaSemana) {
    // Verificar se diaSemana está definido
    if (!diaSemana) {
        console.error('Dia da semana não definido para o treino ID:', treinoId);
        const exerciciosContainer = document.getElementById(`exercicios-${treinoId}`);
        exerciciosContainer.innerHTML = `
            <div class="erro">
                <p>Não foi possível carregar os exercícios. Dia da semana não especificado.</p>
            </div>
        `;
        return;
    }
    
    console.log(`Carregando exercícios para o treino ${treinoId}, dia "${diaSemana}"`);
    
    // Usa formato exato retornado; evita divergências de normalização
    
    const exerciciosContainer = document.getElementById(`exercicios-${treinoId}`);
    exerciciosContainer.innerHTML = '<div class="loading">Carregando exercícios...</div>';
    
    // URL da API - usar o valor exato do dia que veio do botão (que foi preservado do valor original do backend)
    const url = `http://localhost:8080/api/exercicios/treino/${treinoId}?diaSemana=${encodeURIComponent(diaSemana)}`;
    console.log('URL da requisição:', url);
    
    const inicioReq = performance.now(); // Métrica simples de latência
    fetch(url)
        .then(response => {
            console.log('[REQ EXERCICIOS] Status:', response.status, '| Dia enviado:', diaSemana, '| URL:', url);
            if (!response.ok) {
                throw new Error(`Falha ao carregar exercícios: ${response.status} ${response.statusText}`);
            }
            return response.text().then(txt => {
                const dur = (performance.now() - inicioReq).toFixed(1);
                console.log(`[REQ EXERCICIOS] Tempo ${dur}ms | Payload bruto (${txt.length} chars)`); // Log diagnóstico
                try {
                    return JSON.parse(txt);
                } catch(e){
                    console.error('Falha parse JSON exercícios:', e, 'Texto recebido:', txt);
                    throw new Error('JSON inválido nos exercícios');
                }
            });
        })
        .then(exercicios => {
            console.log(`Exercícios recebidos para o dia "${diaSemana}" (qtde=${Array.isArray(exercicios)?exercicios.length:'N/A'}):`, exercicios);
            if (Array.isArray(exercicios)) { // Log enumerado por exercício
                exercicios.forEach((ex,i)=>{
                    console.log(`[EX ${i}] id=${ex.id} nome=${ex.nome} dia='${ex.diaSemana}' grupo=${ex.grupoMuscular}`);
                });
            }
            
            if (!Array.isArray(exercicios)) {
                console.error('Resposta de exercícios não é um array:', exercicios);
                exerciciosContainer.innerHTML = '<p>Erro ao processar dados de exercícios.</p>';
                return;
            }
            
            if (Array.isArray(exercicios) && exercicios.length === 0) {
                // Formatar dia para exibição
                const diaExibicao = formatarDiaParaExibicao(diaSemana);
                
                exerciciosContainer.innerHTML = `
                    <p>Nenhum exercício cadastrado para ${diaExibicao}.</p>
                `;
                return;
            }
            
            // Mantém ordem recebida (campo 'ordem' não existe no modelo atual)
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
                            ${exercicio.descanso ? `
                            <div class="exercicio-detalhe">
                                <div class="detalhe-label">Descanso</div>
                                <div class="detalhe-valor">${exercicio.descanso}s</div>
                            </div>` : ''}
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
            exerciciosContainer.innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar os exercícios: ${error.message}</p>
                </div>
            `;
        });
}

/**
 * Formata o dia da semana para exibição amigável ao usuário
 * @param {string} dia - O dia da semana a ser formatado
 * @returns {string} - O dia da semana formatado
 */
function formatarDiaParaExibicao(dia) {
    if (!dia) return 'Dia não especificado';
    
    // Mapeamento para exibição
    const mapeamentoDias = {
        'SEGUNDA': 'Segunda',
        'TERCA': 'Terça',
        'TERÇA': 'Terça',
        'QUARTA': 'Quarta',
        'QUINTA': 'Quinta',
        'SEXTA': 'Sexta',
        'SABADO': 'Sábado',
        'SÁBADO': 'Sábado',
        'DOMINGO': 'Domingo'
    };
    
    return mapeamentoDias[dia] || dia;
}
