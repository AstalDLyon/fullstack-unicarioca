/**
 * medidas-script.js
 * 
 * Este arquivo é responsável por gerenciar a visualização e interação com o histórico de medidas
 * corporais dos alunos no sistema UniGym. Ele carrega e exibe medidas em diferentes formatos,
 * incluindo resumo, histórico completo e gráficos de evolução.
 * 
 * Funcionalidades principais:
 * - Verificação de autenticação do usuário
 * - Redirecionamento para login se não autenticado
 * - Carregamento da última medida registrada
 * - Carregamento do histórico completo de medidas
 * - Geração de gráficos de evolução (peso, gordura, medidas, IMC)
 * - Sistema de abas para navegação entre visualizações
 * - Funcionalidade de logout
 * 
 * Funções:
 * - carregarUltimaMedida: Busca e exibe a medida mais recente do aluno
 * - carregarHistoricoMedidas: Busca e exibe o histórico completo de medidas em formato tabular
 * - carregarGraficos: Inicializa e popula os gráficos de evolução das medidas
 * - configurarGrafico: Configura um gráfico ChartJS com dados específicos
 * - calcularIMC: Calcula o IMC baseado em peso e altura
 * - getStatusIMC: Determina a classificação do IMC calculado
 * 
 * Dependências:
 * - Requer localStorage com 'usuarioId' e 'usuarioNome'
 * - API backend: /api/medidas/aluno/{id}, /api/medidas/aluno/{id}/ultima
 * - Biblioteca ChartJS para geração de gráficos
 */

document.addEventListener('DOMContentLoaded', function() {
    // Verificar se o usuário está logado
    const usuarioId = localStorage.getItem('usuarioId');
    const usuarioNome = localStorage.getItem('usuarioNome');
    
    console.log('ID do usuário recuperado para medidas:', usuarioId);
    console.log('Nome do usuário recuperado para medidas:', usuarioNome);
    
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
    
    // Configurar abas
    const tabs = document.querySelectorAll('.tab-btn');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // Remover classe ativa de todas as abas
            tabs.forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(content => {
                content.classList.remove('active');
            });
            
            // Adicionar classe ativa à aba clicada
            this.classList.add('active');
            document.getElementById(`tab-${this.dataset.tab}`).classList.add('active');
        });
    });
    
    // Carregar medidas
    carregarUltimaMedida(usuarioId);
    carregarHistoricoMedidas(usuarioId);
    carregarGraficos(usuarioId);
});

function carregarUltimaMedida(alunoId) {
    console.log('Carregando última medida para o aluno ID:', alunoId);
    
    const container = document.getElementById('ultima-medida-container');
    container.innerHTML = '<div class="loading">Carregando medidas...</div>';
    
    fetch(`http://localhost:8080/api/medidas/aluno/${alunoId}/ultima`)
        .then(response => {
            console.log('Status da resposta medidas/ultima:', response.status);
            if (!response.ok) {
                if (response.status === 404) {
                    console.log('Nenhuma medida encontrada (404)');
                    return null;
                }
                throw new Error(`Falha ao carregar medidas: ${response.status} ${response.statusText}`);
            }
            return response.text().then(text => {
                // Log do texto bruto para depuração
                console.log('Resposta bruta última medida (primeiros 500 caracteres):', text.substring(0, 500));
                try {
                    return JSON.parse(text);
                } catch (e) {
                    console.error('Erro ao parsear JSON da última medida:', e);
                    throw new Error('Erro ao processar resposta do servidor');
                }
            });
        })
        .then(medida => {
            console.log('Última medida recebida:', medida);
            const container = document.getElementById('ultima-medida-container');
            
            if (!medida) {
                container.innerHTML = `
                    <div class="sem-medidas">
                        <h3>Nenhuma medida registrada</h3>
                        <p>Fale com seu instrutor para realizar uma avaliação física.</p>
                    </div>
                `;
                return;
            }
            
            const dataFormatada = new Date(medida.data).toLocaleDateString('pt-BR');
            
            container.innerHTML = `
                <div class="ultima-medida">
                    <h3>Última Avaliação - ${dataFormatada}</h3>
                    <div class="medidas-grid">
                        <div class="medida-item">
                            <div class="medida-label">Peso</div>
                            <div class="medida-valor">${medida.peso} kg</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Altura</div>
                            <div class="medida-valor">${medida.altura} m</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">IMC</div>
                            <div class="medida-valor">${medida.imc ? medida.imc.toFixed(1) : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">% Gordura</div>
                            <div class="medida-valor">${medida.percentualGordura ? medida.percentualGordura.toFixed(1) + '%' : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Braço</div>
                            <div class="medida-valor">${medida.circunferenciaBraco ? medida.circunferenciaBraco + 'cm' : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Peitoral</div>
                            <div class="medida-valor">${medida.circunferenciaPeitoral ? medida.circunferenciaPeitoral + 'cm' : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Cintura</div>
                            <div class="medida-valor">${medida.circunferenciaCintura ? medida.circunferenciaCintura + 'cm' : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Quadril</div>
                            <div class="medida-valor">${medida.circunferenciaQuadril ? medida.circunferenciaQuadril + 'cm' : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Coxa</div>
                            <div class="medida-valor">${medida.circunferenciaCoxa ? medida.circunferenciaCoxa + 'cm' : '-'}</div>
                        </div>
                        <div class="medida-item">
                            <div class="medida-label">Panturrilha</div>
                            <div class="medida-valor">${medida.circunferenciaPanturrilha ? medida.circunferenciaPanturrilha + 'cm' : '-'}</div>
                        </div>
                    </div>
                </div>
            `;
        })
        .catch(error => {
            console.error('Erro ao carregar última medida:', error);
            document.getElementById('ultima-medida-container').innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar suas medidas. Por favor, tente novamente mais tarde.</p>
                </div>
            `;
        });
}

function carregarHistoricoMedidas(alunoId) {
    console.log('Carregando histórico de medidas para o aluno ID:', alunoId);
    
    const container = document.getElementById('historico-container');
    container.innerHTML = '<div class="loading">Carregando histórico de medidas...</div>';
    
    fetch(`http://localhost:8080/api/medidas/aluno/${alunoId}`)
        .then(response => {
            console.log('Status da resposta histórico medidas:', response.status);
            if (!response.ok) {
                throw new Error(`Falha ao carregar histórico de medidas: ${response.status} ${response.statusText}`);
            }
            return response.text().then(text => {
                // Log do texto bruto para depuração
                console.log('Resposta bruta histórico (primeiros 500 caracteres):', text.substring(0, 500));
                try {
                    return JSON.parse(text);
                } catch (e) {
                    console.error('Erro ao parsear JSON do histórico:', e);
                    throw new Error('Erro ao processar resposta do servidor');
                }
            });
        })
        .then(medidas => {
            const container = document.getElementById('historico-container');
            
            if (medidas.length === 0) {
                container.innerHTML = `
                    <div class="sem-medidas">
                        <h3>Nenhuma medida registrada no histórico</h3>
                    </div>
                `;
                return;
            }
            
            // Ordenar medidas por data (mais recente primeiro)
            medidas.sort((a, b) => new Date(b.data) - new Date(a.data));
            
            let html = `
                <div class="historico-header">
                    <h3 class="historico-title">Histórico de Medidas</h3>
                    <div class="historico-filtros">
                        <button class="filtro-btn active" data-periodo="todos">Todos</button>
                        <button class="filtro-btn" data-periodo="6m">Últimos 6 meses</button>
                        <button class="filtro-btn" data-periodo="3m">Últimos 3 meses</button>
                    </div>
                </div>
                
                <table class="historico-table">
                    <thead>
                        <tr>
                            <th>Data</th>
                            <th>Peso (kg)</th>
                            <th>% Gordura</th>
                            <th>IMC</th>
                            <th>Braço (cm)</th>
                            <th>Peitoral (cm)</th>
                            <th>Cintura (cm)</th>
                            <th>Quadril (cm)</th>
                        </tr>
                    </thead>
                    <tbody>
            `;
            
            medidas.forEach(medida => {
                const data = new Date(medida.data).toLocaleDateString('pt-BR');
                
                html += `
                    <tr data-data="${medida.data}">
                        <td>${data}</td>
                        <td>${medida.peso}</td>
                        <td>${medida.percentualGordura ? medida.percentualGordura.toFixed(1) + '%' : '-'}</td>
                        <td>${medida.imc ? medida.imc.toFixed(1) : '-'}</td>
                        <td>${medida.circunferenciaBraco || '-'}</td>
                        <td>${medida.circunferenciaPeitoral || '-'}</td>
                        <td>${medida.circunferenciaCintura || '-'}</td>
                        <td>${medida.circunferenciaQuadril || '-'}</td>
                    </tr>
                `;
            });
            
            html += `
                    </tbody>
                </table>
            `;
            
            container.innerHTML = html;
            
            // Adicionar funcionalidade aos botões de filtro
            const botoesFilto = container.querySelectorAll('.filtro-btn');
            botoesFilto.forEach(botao => {
                botao.addEventListener('click', function() {
                    // Remover classe ativa de todos os botões
                    botoesFilto.forEach(b => b.classList.remove('active'));
                    
                    // Adicionar classe ativa ao botão clicado
                    this.classList.add('active');
                    
                    // Aplicar filtro
                    const periodo = this.dataset.periodo;
                    aplicarFiltroPeriodo(periodo);
                });
            });
            
            // Função para aplicar filtro por período
            function aplicarFiltroPeriodo(periodo) {
                const linhas = container.querySelectorAll('tbody tr');
                const hoje = new Date();
                
                linhas.forEach(linha => {
                    const dataMedida = new Date(linha.dataset.data);
                    let mostrar = true;
                    
                    if (periodo === '3m') {
                        const tresMesesAtras = new Date(hoje);
                        tresMesesAtras.setMonth(hoje.getMonth() - 3);
                        mostrar = dataMedida >= tresMesesAtras;
                    } else if (periodo === '6m') {
                        const seisMesesAtras = new Date(hoje);
                        seisMesesAtras.setMonth(hoje.getMonth() - 6);
                        mostrar = dataMedida >= seisMesesAtras;
                    }
                    
                    linha.style.display = mostrar ? '' : 'none';
                });
            }
        })
        .catch(error => {
            console.error('Erro ao carregar histórico:', error);
            document.getElementById('historico-container').innerHTML = `
                <div class="erro">
                    <p>Ocorreu um erro ao carregar o histórico de medidas. Por favor, tente novamente mais tarde.</p>
                </div>
            `;
        });
}

function carregarGraficos(alunoId) {
    fetch(`http://localhost:8080/api/medidas/aluno/${alunoId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Falha ao carregar dados para gráficos');
            }
            return response.json();
        })
        .then(medidas => {
            if (medidas.length === 0) {
                return;
            }
            
            // Ordenar medidas por data (mais antiga primeiro para o gráfico)
            medidas.sort((a, b) => new Date(a.data) - new Date(b.data));
            
            // Preparar dados para os gráficos
            const datas = medidas.map(m => new Date(m.data).toLocaleDateString('pt-BR'));
            const pesos = medidas.map(m => m.peso);
            const gorduras = medidas.map(m => m.percentualGordura);
            const imcs = medidas.map(m => m.imc);
            
            // Gráfico de Peso
            new Chart(document.getElementById('pesoChart'), {
                type: 'line',
                data: {
                    labels: datas,
                    datasets: [{
                        label: 'Peso (kg)',
                        data: pesos,
                        backgroundColor: 'rgba(160, 31, 34, 0.2)',
                        borderColor: 'rgba(160, 31, 34, 1)',
                        borderWidth: 2,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
            
            // Gráfico de % de Gordura
            new Chart(document.getElementById('gorduraChart'), {
                type: 'line',
                data: {
                    labels: datas,
                    datasets: [{
                        label: '% de Gordura',
                        data: gorduras,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 2,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
            
            // Gráfico de IMC
            new Chart(document.getElementById('imcChart'), {
                type: 'line',
                data: {
                    labels: datas,
                    datasets: [{
                        label: 'IMC',
                        data: imcs,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 2,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
            
            // Gráfico de Medidas
            const medidasChart = new Chart(document.getElementById('medidasChart'), {
                type: 'line',
                data: {
                    labels: datas,
                    datasets: [
                        {
                            label: 'Braço (cm)',
                            data: medidas.map(m => m.circunferenciaBraco),
                            borderColor: 'rgba(255, 99, 132, 1)',
                            backgroundColor: 'rgba(255, 99, 132, 0.1)',
                            borderWidth: 2,
                            tension: 0.3
                        },
                        {
                            label: 'Peitoral (cm)',
                            data: medidas.map(m => m.circunferenciaPeitoral),
                            borderColor: 'rgba(54, 162, 235, 1)',
                            backgroundColor: 'rgba(54, 162, 235, 0.1)',
                            borderWidth: 2,
                            tension: 0.3
                        },
                        {
                            label: 'Cintura (cm)',
                            data: medidas.map(m => m.circunferenciaCintura),
                            borderColor: 'rgba(255, 206, 86, 1)',
                            backgroundColor: 'rgba(255, 206, 86, 0.1)',
                            borderWidth: 2,
                            tension: 0.3
                        },
                        {
                            label: 'Quadril (cm)',
                            data: medidas.map(m => m.circunferenciaQuadril),
                            borderColor: 'rgba(75, 192, 192, 1)',
                            backgroundColor: 'rgba(75, 192, 192, 0.1)',
                            borderWidth: 2,
                            tension: 0.3
                        }
                    ]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Erro ao carregar dados para gráficos:', error);
        });
}