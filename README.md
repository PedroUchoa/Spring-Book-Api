<h1 align="center"> Book Saver API </h1>

# Resumo do projeto
O serviço é um salvador de livros que utiliza Sql como database para salvar tanto os livros como os usuarios, 
como usuario você vai ter uma lista de livros que poderam ser salvos na sua lista de favoritos. 
Ao utilizar a aplicação você poderá checar as rotas com a documentação gerada pelo Springdoc (Mais informações na área de utilização)

# Endpoints

![image](https://github.com/PedroUchoa/Spring-Book-Api/assets/98981764/770fae19-b0bf-4894-b1d4-e950ba0a604c)


## ✔️ Bibliotecas e tecnologias utilizadas

- ``Java 19``
- ``Maven``
- ``Spring 3.1.3``
- ``Sql``
- ``Flyway``
- ``Junit``
- ``SpringDoc``
- ``Lombok``
- ``DevTools``
- ``Spring Security``
- ``Auth0 jwt``

# Features 
1. Operação de Crud do user
2. Operação de Crud do book
3. Adicionar um Book a lista de favoritos do user
4. Get das categorias de um Book
5. Controller de Autenticação 

# Instalação
1. Faça a clonagem do projeto
2. Abra o projeto na sua IDE e faça a instalação das dependências através do Maven
3. Mude a configuração do seu SQL na application.properties e tenha um banco com o nome "booksaver_api" criado
4. Rode o projeto e as migrations
5. Acesse http://localhost:8080/swagger-ui.html para ver a documentação
6. Crie um usuario e senha para testes
