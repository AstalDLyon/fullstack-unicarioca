// Navegação suave ao clicar nos links do menu
document.querySelectorAll('nav a').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const targetId = this.getAttribute('href').substring(1);
        const targetSection = document.getElementById(targetId);
        if (targetSection) {
            targetSection.scrollIntoView({ behavior: 'smooth' });
        }
    });
});

// Simulação de busca de unidades
document.querySelector('.unidades button').addEventListener('click', () => {
    const input = document.querySelector('.unidades input').value.trim();
    if (input) {
        alert(`Buscando unidades próximas de: ${input}`);
        // Aqui você poderia integrar com uma API de mapas ou geolocalização
    } else {
        alert('Por favor, digite uma cidade ou CEP.');
    }
});

// Destaque ao selecionar plano
document.querySelectorAll('.plano-card').forEach(card => {
    card.addEventListener('click', () => {
        alert(`Você selecionou o plano: ${card.querySelector('h3').textContent}`);
    });
});