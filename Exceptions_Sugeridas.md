# Lista de Exceptions Sugeridas (Projeto Finanças)

Aqui estão as exceções recomendadas para você criar manualmente no seu pacote `com.DespesasFinanceiras.V1.Exceptions`. Você pode criá-las seguindo a mesma estrutura da sua `NotFoundException` e depois mapeá-las no `RestExceptionHandler`.

### 1. `RegraDeNegocioException`
* **Herança:** `RuntimeException`
* **Status HTTP ideal no Handler:** `400 Bad Request` ou `422 Unprocessable Entity`
* **Quando lançar:** Violações gerais das regras de negócio do sistema. 
  * *Exemplo:* Tentar deletar uma despesa que já tem a situação `PAGA`.

### 2. `ValorInvalidoException`
* **Herança:** `RuntimeException`
* **Status HTTP ideal no Handler:** `400 Bad Request`
* **Quando lançar:** Inconsistências nos valores monetários (campo `valor` em Despesas/Receitas).
  * *Exemplo:* O usuário tenta salvar uma Receita ou Despesa com o valor `0.00` ou um valor negativo (ex: `-100.00`).

### 3. `StatusInvalidoException`
* **Herança:** `RuntimeException`
* **Status HTTP ideal no Handler:** `400 Bad Request`
* **Quando lançar:** Uso incorreto do seu `SituacaoEnum`.
  * *Exemplo:* Tentar cadastrar uma `DespesasEntity` e enviar no JSON a situação `RECEBIDO` (que faz sentido apenas para Receitas).

### 4. `RegistroDuplicadoException`
* **Herança:** `RuntimeException`
* **Status HTTP ideal no Handler:** `409 Conflict`
* **Quando lançar:** Para evitar que cadastros duplos aconteçam por acidente.
  * *Exemplo:* O sistema detecta que já existe uma Despesa cadastrada com exatamente o mesmo `name`, `valor` e `localDateTime`.

### 5. `DataInvalidaException`
* **Herança:** `RuntimeException`
* **Status HTTP ideal no Handler:** `400 Bad Request`
* **Quando lançar:** Erros de validação relacionados às datas (`LocalDateTime`).
  * *Exemplo:* Tentar registrar uma despesa com data de lançamento/vencimento incompatível com as regras.

### 6. `OperacaoNaoPermitidaException`
* **Herança:** `RuntimeException`
* **Status HTTP ideal no Handler:** `403 Forbidden`
* **Quando lançar:** Bloqueios de segurança ou regras rígidas de acesso a certas funções.
  * *Exemplo:* Tentar alterar uma movimentação que pertence a um mês que já foi "fechado" ou consolidado na contabilidade.

---
**Dica para a criação na mão:**
Basta criar a classe, fazer o `extends RuntimeException`, gerar o construtor que recebe a `String message` (com o `super(message)`) e depois mapeá-las no seu `@ControllerAdvice` (`RestExceptionHandler`).
