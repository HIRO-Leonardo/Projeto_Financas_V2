# Sistema de Controle de Despesas Pessoais 

# Screenshots
![Captura de tela de 2026-10-02 20-19-57.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-19-57.png)
![Captura de tela de 2026-10-02 20-20-02.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-20-02.png)
![Captura de tela de 2026-10-02 20-20-07.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-20-07.png)
![Captura de tela de 2026-10-02 20-20-11.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-20-11.png)
![Captura de tela de 2026-10-02 20-20-16.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-20-16.png)
![Captura de tela de 2026-10-02 20-20-21.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-20-21.png)
![Captura de tela de 2026-10-02 20-20-27.png](../../../../Imagens/Capturas%20de%20tela/Captura%20de%20tela%20de%202026-10-02%2020-20-27.png)






Um sistema completo para gerenciamento de finanças pessoais, projetado para uso local, permitindo o controle rigoroso de receitas, despesas, saldo e geração de relatórios.

##  Tecnologias Utilizadas

### Back-end
- **Java 21**
- **Spring Boot 3.x**
  - Spring Web (MVC)
  - Spring Data JPA
  - Spring Validation
- **PostgreSQL** (Banco de dados relacional)
- **Lombok** (Redução de boilerplate)
- **Springdoc OpenAPI / Swagger** (Documentação da API)

### Front-end
- **Angular** (TypeScript, HTML, CSS)

---

## Funcionalidades

### Implementadas
* **Gestão de Despesas e Receitas:** Operações completas de CRUD (Create, Read, Update, Delete com *soft-delete*).
* **Filtros Avançados:** Busca combinada por mês, categoria e situação (pago/pendente/atrasado).
* **Cálculo de Saldo:** Subtração automática de despesas sobre as receitas, com endpoint dedicado para consulta.
* **Dashboard e Relatórios:**
  * Total gasto por categoria no mês.
  * Comparativo financeiro mês a mês.
  * Relação de receitas vs despesas mensais.
* **Paginação:** Listagens otimizadas utilizando `Pageable`.
* **Tratamento de Exceções:** Sistema centralizado com `@ControllerAdvice` e exceções customizadas para respostas claras (ex: 404, 400).
* **Validações:** Regras de negócio restritas (valores não negativos, datas obrigatórias, etc).
* **Documentação de API:** Endpoints mapeados e testáveis via Swagger UI.

### Próximos Passos (Roadmap)
* **Testes:** Implementação de testes unitários com JUnit e Mockito para as camadas de serviço.
* **Autores:** Adição da funcionalidade de registrar o "Autor" ou responsável por cada despesa.
* **Dockerização:** Criação de ambiente via `docker-compose` para o PostgreSQL e a aplicação com volumes nomeados.
* **Autenticação:** Implementação futura de Spring Security com JWT.

---

##  Como Executar o Projeto

### Pré-requisitos
* Java 21+ instalado.
* Maven instalado.
* Node.js e Angular CLI instalados.
* PostgreSQL rodando localmente (ou via Docker).

### 1. Configurando o Banco de Dados
Crie um banco de dados no PostgreSQL e configure as credenciais no arquivo `src/main/resources/application.properties` (ou `.env` caso esteja utilizando variáveis de ambiente):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### 2. Rodando o Back-end
Na raiz do projeto (onde está o `pom.xml`), execute:
```bash
# Baixar dependências e compilar
./mvnw clean install

# Rodar a aplicação
./mvnw spring-boot:run
```
A API estará rodando em `http://localhost:8080`.

### 3. Rodando o Front-end
Navegue até a pasta `frontend` e execute:
```bash
npm install
ng serve
```
O front-end estará disponível em `http://localhost:4200`.

---

##  Documentação da API (Swagger)

Com o back-end em execução, você pode acessar a interface do Swagger para explorar e testar os endpoints através do link:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

##  Desenvolvedor
Projeto de autoria e uso pessoal, desenvolvido de forma iterativa focando em boas práticas, código limpo e arquitetura robusta com Spring Boot e Angular.
