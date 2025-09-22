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



// Destaque ao selecionar plano
document.querySelectorAll('.plano-card').forEach(card => {
    card.addEventListener('click', () => {
        alert(`Você selecionou o plano: ${card.querySelector('h3').textContent}`);
    });
});