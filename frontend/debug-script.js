/**
 * debug-script.js
 * 
 * Este arquivo é uma ferramenta de diagnóstico para o sistema UniGym.
 * Ele cria uma interface de teste que permite verificar o funcionamento das APIs
 * e do sistema de autenticação durante o desenvolvimento e depuração.
 * 
 * Funcionalidades principais:
 * - Exibição de informações do usuário logado (ID e nome)
 * - Testes diretos das APIs de treinos, exercícios e medidas
 * - Visualização dos resultados das requisições
 * - Log detalhado de erros e respostas
 * 
 * Funções:
 * - addResult: Adiciona uma entrada de resultado ao painel de diagnóstico
 * - Handlers para testes de APIs específicas:
 *   - API de Treinos (ativos e todos)
 *   - API de Exercícios
 *   - API de Medidas (última e histórico)
 * 
 * Observação: Este arquivo é destinado apenas para ambiente de desenvolvimento
 * e não deve ser incluído em produção.
 */


document.addEventListener('DOMContentLoaded', function() {
    const testSection = document.createElement('div');
    testSection.className = 'test-section';
    testSection.style.padding = '20px';
    testSection.style.margin = '20px';
    testSection.style.backgroundColor = '#f5f5f5';
    testSection.style.border = '1px solid #ddd';
    testSection.style.borderRadius = '5px';

    const heading = document.createElement('h2');
    heading.textContent = 'Diagnóstico e Teste';
    testSection.appendChild(heading);

    // Informações do usuário logado
    const userInfo = document.createElement('div');
    userInfo.innerHTML = `
        <h3>Informações do Usuário</h3>
        <p><strong>ID:</strong> <span id="test-user-id">Carregando...</span></p>
        <p><strong>Nome:</strong> <span id="test-user-name">Carregando...</span></p>
    `;
    testSection.appendChild(userInfo);

    // Testes de API
    const apiTests = document.createElement('div');
    apiTests.innerHTML = `
        <h3>Testes de API</h3>
        <button id="test-treinos-btn" class="test-btn">Testar API de Treinos</button>
        <button id="test-exercicios-btn" class="test-btn">Testar API de Exercícios</button>
        <button id="test-medidas-btn" class="test-btn">Testar API de Medidas</button>
        <div id="test-results" style="margin-top: 10px; max-height: 300px; overflow-y: auto; border: 1px solid #ccc; padding: 10px;"></div>
    `;
    testSection.appendChild(apiTests);

    // Estilo para botões
    const style = document.createElement('style');
    style.textContent = `
        .test-btn {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 8px 16px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 4px;
        }
    `;
    document.head.appendChild(style);

    // Adicionar a seção de teste antes do footer
    const footer = document.querySelector('footer');
    document.body.insertBefore(testSection, footer);

    // Preencher informações do usuário
    const userId = localStorage.getItem('usuarioId');
    const userName = localStorage.getItem('usuarioNome');
    document.getElementById('test-user-id').textContent = userId || 'Não encontrado';
    document.getElementById('test-user-name').textContent = userName || 'Não encontrado';

    // Função para adicionar resultado ao painel
    function addResult(message, isError = false) {
        const resultDiv = document.getElementById('test-results');
        const result = document.createElement('div');
        result.style.color = isError ? 'red' : 'green';
        result.style.marginBottom = '5px';
        result.style.borderBottom = '1px solid #eee';
        result.style.paddingBottom = '5px';
        result.innerHTML = `<strong>${new Date().toLocaleTimeString()}</strong>: ${message}`;
        resultDiv.prepend(result);
    }

    // Teste de API de Treinos
    document.getElementById('test-treinos-btn').addEventListener('click', function() {
        if (!userId) {
            addResult('Erro: ID do usuário não encontrado', true);
            return;
        }

        addResult('Testando API de Treinos...');
        
        // Testar treinos ativos
        fetch(`http://localhost:8080/api/treinos/aluno/${userId}/ativos`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Status: ${response.status} ${response.statusText}`);
                }
                // Primeiro obter o texto bruto para diagnóstico
                return response.text().then(text => {
                    console.log('Resposta bruta treinos ativos:', text.substring(0, 1000) + '... (truncado)');
                    try {
                        // Tentar parsear o JSON
                        return JSON.parse(text);
                    } catch (e) {
                        console.error('Erro ao parsear JSON:', e);
                        console.error('Trecho do JSON com problema:', text.substring(Math.max(0, e.position - 50), e.position + 50));
                        throw new Error(`Erro ao parsear JSON: ${e.message}`);
                    }
                });
            })
            .then(data => {
                addResult(`Sucesso! Treinos ativos: ${data.length} encontrados`);
                console.log('Treinos ativos:', data);
            })
            .catch(error => {
                addResult(`Erro na API de Treinos Ativos: ${error.message}`, true);
                console.error('Erro em treinos ativos:', error);
            });

        // Testar todos os treinos
        fetch(`http://localhost:8080/api/treinos/aluno/${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Status: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                addResult(`Sucesso! Todos os treinos: ${data.length} encontrados`);
                console.log('Todos os treinos:', data);
            })
            .catch(error => {
                addResult(`Erro na API de Todos os Treinos: ${error.message}`, true);
                console.error('Erro em todos os treinos:', error);
            });
    });

    // Teste de API de Exercícios
    document.getElementById('test-exercicios-btn').addEventListener('click', function() {
        if (!userId) {
            addResult('Erro: ID do usuário não encontrado', true);
            return;
        }

        addResult('Testando API de Exercícios...');
        
        // Primeiro obter os treinos do usuário
        fetch(`http://localhost:8080/api/treinos/aluno/${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Status: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(treinos => {
                if (treinos.length === 0) {
                    addResult('Nenhum treino encontrado para testar exercícios');
                    return;
                }

                // Testar exercícios do primeiro treino
                const treinoId = treinos[0].id;
                addResult(`Testando exercícios do treino ID ${treinoId}...`);
                
                return fetch(`http://localhost:8080/api/exercicios/treino/${treinoId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`Status: ${response.status} ${response.statusText}`);
                        }
                        return response.json();
                    })
                    .then(exercicios => {
                        addResult(`Sucesso! Exercícios: ${exercicios.length} encontrados`);
                        console.log('Exercícios:', exercicios);
                    });
            })
            .catch(error => {
                addResult(`Erro na API de Exercícios: ${error.message}`, true);
                console.error('Erro em exercícios:', error);
            });
    });

    // Teste de API de Medidas
    document.getElementById('test-medidas-btn').addEventListener('click', function() {
        if (!userId) {
            addResult('Erro: ID do usuário não encontrado', true);
            return;
        }

        addResult('Testando API de Medidas...');
        
        // Testar última medida
        fetch(`http://localhost:8080/api/medidas/aluno/${userId}/ultima`)
            .then(response => {
                const status = `Status: ${response.status} ${response.statusText}`;
                if (response.status === 404) {
                    addResult(`Nenhuma medida encontrada (${status})`);
                    return null;
                }
                if (!response.ok) {
                    throw new Error(status);
                }
                return response.json();
            })
            .then(medida => {
                if (medida) {
                    addResult('Sucesso! Última medida encontrada');
                    console.log('Última medida:', medida);
                }
            })
            .catch(error => {
                addResult(`Erro na API de Última Medida: ${error.message}`, true);
                console.error('Erro em última medida:', error);
            });

        // Testar todas as medidas
        fetch(`http://localhost:8080/api/medidas/aluno/${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Status: ${response.status} ${response.statusText}`);
                }
                // Primeiro obter o texto bruto para diagnóstico
                return response.text().then(text => {
                    console.log('Resposta bruta medidas:', text.substring(0, 1000) + '... (truncado)');
                    try {
                        // Tentar parsear o JSON
                        return JSON.parse(text);
                    } catch (e) {
                        console.error('Erro ao parsear JSON de medidas:', e);
                        console.error('Trecho do JSON com problema:', text.substring(Math.max(0, e.position - 50), e.position + 50));
                        throw new Error(`Erro ao parsear JSON de medidas: ${e.message}`);
                    }
                });
            })
            .then(medidas => {
                addResult(`Sucesso! Todas as medidas: ${medidas.length} encontradas`);
                console.log('Todas as medidas:', medidas);
            })
            .catch(error => {
                addResult(`Erro na API de Todas as Medidas: ${error.message}`, true);
                console.error('Erro em todas as medidas:', error);
            });
    });
});