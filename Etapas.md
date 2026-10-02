# Roadmap — Sistema de Controle de Despesas Pessoais

**Stack:** Spring Boot + PostgreSQL + Spring Data JPA
**Contexto:** Projeto pessoal, uso local, sem autenticação (por enquanto)
 
---

## ✅ Já feito

- [x] Entidades `DespesasEntity` e `ReceitaEntity`
- [x] Enums de categoria (despesa e receita) e `SituacaoEnum`
- [x] CRUD completo: create, listar, update, delete (com soft-delete)
- [x] Query de soma (`SUM`) por situação com `@Query`
---

## 🔲 Próximas etapas

### 1. Filtros na listagem
- [x] Filtro por mes
- [x] Filtro por categoria
- [x] Filtro por situação (pago/pendente/atrasado)
- [x] Combinação de múltiplos filtros
### 2. Cálculo de saldo
- [x] Soma total de receitas − soma total de despesas
- [x] Endpoint `GET /saldo` retornando o resultado calculado
### 3. Relatórios / Dashboard
- [x] Total gasto por categoria no mês
- [x] Comparativo mês a mês (mês atual vs mês anterior)
- [x] Receita vs despesa por mês
### 4. Validações mais completas
- [x] Revisar `@Valid` nos DTOs (valor não negativo, data obrigatória, descrição obrigatória, etc.)
### 5. Tratamento de exceções centralizado
- [x] Criar `@ControllerAdvice` + `@ExceptionHandler`
- [x] Tratar "não encontrado" → 404
- [X] Tratar erro de validação → 400
- [x] Criar exceções customizadas (ex: `DespesaNaoEncontradaException`)
### 6. Paginação
- [x] Usar `Pageable` do Spring Data nas listagens
### 7. Documentação com Swagger/Gemini
- [x] Configurar springdoc-openapi
- [x] Testar endpoints via Swagger UI
### 8. Testes
- [ ] Testes unitários dos Services (JUnit + Mockito)
- [ ] Cobrir: criar, atualizar, deletar, calcular saldo
### 9. Docker
- [ ] `docker-compose` com Postgres + aplicação
- [ ] Configurar volume nomeado no Postgres (evitar perda de dados)
- [ ] Variáveis de ambiente para configuração (DB_URL, DB_USER, DB_PASSWORD)
---

## Ordem sugerida

```
Filtros → Saldo → Relatórios → Exceções centralizadas → Paginação
→ Swagger → Testes → Docker
```

**Observação:** exceções centralizadas podem ser adiantadas — é rápido de implementar e evita tratamento de erro 