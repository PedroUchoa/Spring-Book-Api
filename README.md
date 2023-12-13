<h1 align="center"> Book Saver API </h1>

# Resumo do projeto
O serviço é um salvador de livros que utiliza SQL como database para salvar livros e usuários. Como usuário, você terá duas roles (Admin e User), onde o User normal apenas pode fazer requisições GET e o ADMIN pode realizar todos os tipos de requisições. O usuário tem acesso a um banco de dados de livros que poderão ser salvos na sua lista de favoritos. Ao utilizar a aplicação, você poderá checar as rotas com a documentação gerada pelo Springdoc. Também contém uma integração com a Google Book API. Com ela, você pode fazer o GET de vários livros diretamente da API do Google. (Mais informações na área de utilização).

# Endpoints

![image](https://github.com/PedroUchoa/Spring-Book-Api/assets/98981764/8bd5f199-b4e9-4af7-8eb1-7d8eb6c25d41)



## ✔️ Bibliotecas e tecnologias utilizadas

- ``Java 19``
- ``Spring 3.1.3``
- ``Maven``
- ``Sql``
- ``Flyway``
- ``Junit``
- ``Mockito``
- ``SpringDoc``
- ``Lombok``
- ``DevTools``
- ``Spring Security``
- ``Auth0 jwt``

# Testes
O projeto também dispõe de testes unitários e de integração, onde foram testados os seus Repositories, Services e Controllers. No teste de Controller, foi utilizado a ferramenta Data Jpa Test para garantir que as queries que conversam com o banco de dados SQL estão retornando o esperado. Nos testes de Service, foram utilizadas as tecnologias Mockito e Junit5 para a certificação de que os métodos estão realizando o seu trabalho corretamente e também retornando os erros esperados ao não serem utilizados conforme o esperado. Para os testes de Controller, foi utilizado o MockMvc e Mockito para a certificação de que os métodos estão retornando os códigos esperados ao serem utilizados.

# Features 
1. Operação de Crud do user
2. Operação de Crud do book
3. Adicionar um book a lista de favoritos do user
4. Get das categorias de um Book
5. Controller de Autenticação
6. Get de Livros diretamente da Google Book Api

# Instalação
1. Faça a clonagem do projeto
2. Abra o projeto na sua IDE e faça a instalação das dependências através do Maven
3. Adicione a configuração do seu Banco de Dados SQL na application.properties
4. Para a utilização da Google Book API adicione sua chave no application.properties
5. Rode o projeto e as migrations
6. Acesse http://localhost:8080/swagger-ui.html para ver a documentação
7. Crie um usuario e senha para testes
