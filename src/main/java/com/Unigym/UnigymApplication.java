package com.Unigym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * UnigymApplication
 * 
 * Classe principal da aplicação Spring Boot que inicializa todo o sistema UniGym.
 * Esta classe contém o método main que inicia o servidor web embutido e carrega
 * o contexto da aplicação Spring, incluindo todos os componentes, controladores,
 * serviços e repositórios necessários para o funcionamento do sistema.
 * 
 * A anotação @SpringBootApplication combina várias outras anotações, incluindo:
 * - @Configuration: Marca a classe como fonte de definições de beans
 * - @EnableAutoConfiguration: Configura automaticamente o Spring baseado nas dependências do classpath
 * - @ComponentScan: Escaneia os componentes dentro do pacote e subpacotes
 */
@SpringBootApplication
public class UnigymApplication {

	/**
	 * Método principal que inicia a aplicação Spring Boot.
	 * 
	 * @param args Argumentos de linha de comando passados para a aplicação
	 */
	public static void main(String[] args) {
		SpringApplication.run(UnigymApplication.class, args);
	}

}
