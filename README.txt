### execução
na raiz do projeto subir a aplicação com 
java -jar ./target/wm-routes-0.0.1-SNAPSHOT.jar

deve estar instalado e inicializado a aplicação mondoDB e caso inicializado em um host e portas diferentes da maquina local 
e porta default deve ser configurado no arquivo application.properties


## interface para criação de malha logistica
METHOD: POST
endpoint: http://<servidor>:<porta>/app/routes/maps/<nome do mapa>
exemplo: http://localhost:8080/app/routes/maps/complex
Body :A B 3
	  A C 2
	  B C 2
	  C D 1
	  B D 3
	  B F 5

## interface para consultar melhor rota
METHOD: GET
endpoint: http://<servidor>:<porta>/app/routes/best-route/<nome do mapa>?origin=<no de origem>&destination=<no de destino>&autonomy=<autonomia>&literPrice=<preco do litro>

exemplo: http://localhost:8080/app/routes/best-route/complex?origin=A&destination=Z&autonomy=1&literPrice=1
	
### Considerações

	Para resolver este problema, levei em consideração para definir a arquitetura tecnológica, a simplicidade de operações ( salvar malha logistica e realizar a busca pela melhor rota), 
	demanda por performance na busca pela melhor rota e a funcionalidade de persistir a malha logistica mesmo após queda do servidor  ou reinicialização do micro serviço.
		 
	Devido a simplicidade de operações e para rápido startup de desenvolvimento sem overhead de tempo de desenvolvimento utilizei um framework auto contido o springboot com 2 webservices, 
	um para criar a malha de logistica e outro para calcular a melhor rota. com respectivos métodos POST e GET.
	
	Para demanda por performance na busca pela melhor rota fiz uso de hashtables para otimizar busca em memória  e adicionada uma feature de sempre que possivel carregar os dados da malha em memória das malhas
	logísticas para otimizar o tempo de busca sem realizar IO em disco.
	
	a persistencia de dados é feita em um servidor mongoDB.
	
	Para injeção de dependencias utilizei o próprio Spring DI já presente no como dependência do SpringBoot. 
	
	Para execução do projeto é necessário uma conexão com um banco de dados MongoDB configurado para servidor e porta
defaults, ou alterá-las no arquivo de configuração application.properties.
	
	Para a manipulação de dados de persistencia e mapeamento de queries foi utilizado o Spring Data já configurado na stack do spring-boot .

	A aplicação deve apresentar uma rápida inicialização  pois alem da utilização de um container leve e frameworks para alta
escalabilidade, foi feito um tradeoff, considerando que a memória principal da máquina será suficientemente para armazenar as 
configurações da malha.
	Os cálculos são prioritariamente feito utilizando os dados de memória com a simulação de um cache na memória quando possivel.

Obs:Para persistir os dados a aplicação utiliza o mongodb e está configurado por default o servidor localhost
  na porta 27017. que é a padrão do MongoDB. Essa configuração pode ser alterada no arquivo application.properties
  
#### Descrição do problema ###################

Entregando mercadorias

O Walmart esta desenvolvendo um novo sistema de logística e sua ajuda é muito importante neste momento. Sua tarefa será desenvolver o novo sistema de entregas visando sempre o menor custo. Para popular sua base de dados o sistema precisa expor um Webservices que aceite o formato de malha logística (exemplo abaixo), nesta mesma requisição o requisitante deverá informar um nome para este mapa. É importante que os mapas sejam persistidos para evitar que a cada novo deploy todas as informações desapareçam. O formato de malha logística é bastante simples, cada linha mostra uma rota: ponto de origem, ponto de destino e distância entre os pontos em quilômetros.

A B 10
B D 15
A C 20
C D 30
B E 50
D E 30

Com os mapas carregados o requisitante irá procurar o menor valor de entrega e seu caminho, para isso ele passará o mapa, nome do ponto de origem, nome do ponto de destino, autonomia do caminhão (km/l) e o valor do litro do combustível, agora sua tarefa é criar este Webservices. Um exemplo de entrada seria mapa SP, origem A, destino D, autonomia 10, valor do litro 2,50; a resposta seria a rota A B D com custo de 6,25.

Você está livre para definir a melhor arquitetura e tecnologias para solucionar este desafio, mas não se esqueça de contar sua motivação no arquivo README que deve acompanhar sua solução, junto com os detalhes de como executar seu programa. Documentação e testes serão avaliados também =) Lembre-se de que iremos executar seu código com malhas beeemm mais complexas, por isso é importante pensar em requisitos não funcionais também!!

Também gostaríamos de acompanhar o desenvolvimento da sua aplicação através do código fonte. Por isso, solicitamos a criação de um repositório que seja compartilhado com a gente. Para o desenvolvimento desse sistema, nós solicitamos que você utilize a sua (ou as suas) linguagem de programação principal. Pode ser Java, JavaScript ou Ruby.

Nós solicitamos que você trabalhe no desenvolvimento desse sistema sozinho e não divulgue a solução desse problema pela internet. 