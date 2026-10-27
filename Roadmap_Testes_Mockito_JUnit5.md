# Roadmap: Testes com JUnit 5 + Mockito

Projeto: Spring Boot 4.1.0, Java 21, Maven, PostgreSQL.
Hoje só existe `V1ApplicationTests.contextLoads()` (`@SpringBootTest`, que exige banco/variáveis de ambiente).

## Visão geral

| Fase | Foco | Ferramentas | Sobe Spring? |
|------|------|-------------|--------------|
| 0 | Preparação | pom, estrutura, perfil `test` | - |
| 1 | Services (unitário) | JUnit 5 + Mockito | Não |
| 2 | Controllers (fatia web) | `@WebMvcTest` + `@MockitoBean` | Parcial |
| 3 | Exception handler / validação | `@WebMvcTest` + MockMvc | Parcial |
| 4 | Repositories (queries) | `@DataJpaTest` + H2 ou Testcontainers | Parcial |
| 5 | Cobertura e CI | JaCoCo, `mvn verify` | - |

Ordem recomendada: 0 → 1 → 2 → 3 → 4 → 5. A fase 1 dá o maior retorno com menor esforço.

---

## Fase 0 – Preparação

1. **Dependências** (`pom.xml`): `spring-boot-starter-test` já vem transitivamente via os starters `*-test` (traz JUnit 5, Mockito, AssertJ). Se quiser deixar explícito, adicione-o com escopo `test`.
2. **Estrutura** espelhando `src/main`, dentro de `src/test/java/com/DespesasFinanceiras/V1/`:
   - `Service/`: `DespesaServiceTest`, `ReceitaServiceTest`, `ContabilidadeServiceTest`
   - `Controller/`: `DespesasControllerTest`, `ReceitaControllerTest`, `ContabilidadeControllerTest`
   - `Infra/`: `RestExceptionHandlerTest`
   - `Repository/`: `DespesasRepositoryTest`, `ReceitaRepositoryTest`
   - `Builders/`: `DespesaTestBuilder`, `ReceitaTestBuilder` (dados de teste reutilizáveis)
3. **Perfil de teste**: crie `src/test/resources/application-test.properties` (H2 em memória ou Testcontainers) para que nenhum teste dependa de `DATA_BASE_URL` etc.
4. **`V1ApplicationTests`**: marque com `@ActiveProfiles("test")` ou remova/`@Disabled` até o banco de teste existir; hoje ele quebra o `mvn test` sem as variáveis de ambiente.
5. **Convenções**: nome `metodo_cenario_resultado` (ex.: `deleteDespesa_idInexistente_lancaExcecao`), padrão Arrange-Act-Assert, `@DisplayName` em português, AssertJ para asserts.

---

## Fase 1 – Testes unitários de Service (prioridade máxima)

Estrutura básica (sem Spring): classe anotada com `@ExtendWith(MockitoExtension.class)`, o repository como `@Mock` e o service sob teste como `@InjectMocks`. Em cada teste: configure o mock com `when(...).thenReturn(...)`, chame o método do service, valide o resultado com AssertJ e confirme as interações com `verify` (incluindo `never()` quando algo não deve ser chamado).

### 1.1 `DespesaServiceTest`
- `listAll` – lista com itens / lista vazia (verifique se lança `NotFoundExceptionTest`).
- `createDespesa` – converte DTO→Entity e chama `save` (use `ArgumentCaptor<DespesasEntity>` para validar campos mapeados, inclusive `autor`).
- `updateDespesa` – id existe (atualiza campos e salva) / id inexistente (exceção).
- `deleteDespesa` – existe (`deleteById` chamado, retorna `true`) / não existe (exceção, `never()` em `deleteById`).
- `saldoTotal` – retorno do repository `null` (esperado `BigDecimal.ZERO`?) e com valor.
- `despesaMes`, `relatorioMensal`, `listBetweenTwoMonthDespesa` – delegação correta de parâmetros e composição da lista de duas listas.
- `findByAutorList`, `findAllByOrderByIdDesc` – use `ArgumentCaptor<Pageable>` e confirme página, tamanho e `Sort` por `id DESC`.

### 1.2 `ReceitaServiceTest`
- Mesmo conjunto de CRUD, saldo por categoria e por situação, busca por mês/ano, comparação entre meses, `findByAutor`.
- Cenários de regra de negócio: `ValorInvalidoExceptionTest`, `DateInvalidExceptionTest`, `RegistroDuplicadoExceptionTest`, `OperationNotAllowedExceptionTest` (cubra cada `throw` com um teste).

### 1.3 `ContabilidadeServiceTest`
- Mocke `ReceitaRepositoryTest` e `DespesasRepositoryTest`; teste `listarDespesas` e `listReceitas` (delegação e retorno).

> **Débito técnico a anotar:** `DespesaServiceTest` lança `RuntimeException` genérica em vez de `NotFoundExceptionTest`, e `RestExceptionHandler.DateInvalid` devolve `CONFLICT` com corpo "400". Escreva o teste do comportamento desejado e corrija o código (TDD) ou documente o atual.

Técnicas Mockito a praticar nesta fase: `when/thenReturn`, `thenThrow`, `verify` (`times`, `never`), `ArgumentCaptor`, `InOrder`, `@Captor`, `BDDMockito` (`given/willReturn`), `lenient()` só quando necessário.

---

## Fase 2 – Testes de Controller (`@WebMvcTest`)

Estrutura básica: classe anotada com `@WebMvcTest(NomeDoController.class)`, com `MockMvc` e `ObjectMapper` injetados via `@Autowired` e o service substituído por `@MockitoBean`.

Pontos de atenção:
- Em Spring Boot 4 use `@MockitoBean` (`org.springframework.test.context.bean.override.mockito`); `@MockBean` foi removido.
- O módulo de teste web fica em `spring-boot-starter-webmvc-test` (já no pom) e `@WebMvcTest` está em `org.springframework.boot.webmvc.test.autoconfigure`. Confirme os imports no IDE.
- `CorsConfig` e `RestExceptionHandlerTest` entram na fatia automaticamente (`@ControllerAdvice`).

Cenários por endpoint (`/despesa`, `/receita`, `/contabilidade`):
- GET lista → 200 + JSON (`jsonPath`).
- GET `/paginacao` → parâmetros `page`/`size` repassados ao service.
- POST válido → status de sucesso; POST inválido (`@Valid`: campos em branco, valor negativo) → 400.
- PUT/DELETE com id existente e inexistente (service com `thenThrow(NotFoundException)`) → 404.
- Path variables e enums (`/saldo/{metodo}` com valor inválido → 400).
- `/buscar/{mes}-{ano}`, `/comparationbetweentwomonth/...`, `/relatorio-mensal-categoria/...`.

---

## Fase 3 – `RestExceptionHandlerTest`

Teste cada `@ExceptionHandler` via um controller mockado que lança a exceção:

| Exceção | Status esperado | Corpo `ErrorDTOTest` |
|---------|-----------------|------------------|
| `NotFoundExceptionTest` | 404 | "404"/"Not Found" |
| `ValorInvalidoExceptionTest` | 400 | "400"/"Bad Request" |
| `RegistroDuplicadoExceptionTest` | 409 | verificar código do corpo |
| `DateInvalidExceptionTest` | hoje 409 (provável bug) | – |
| `OperationNotAllowedExceptionTest` | 403 | "403"/"Forbidden" |

Use `@ParameterizedTest` + `@MethodSource` para evitar repetição.

---

## Fase 4 – Repositories (`@DataJpaTest`)

- Queries customizadas: `sumByTipo`, `findByAnoMesDespesa`, `totalPorCategoriaPorMes`, `selectBetweenTwoMonthDespesa`, `selectAllDespesasPerMonth`, `listAllReceitaPerMonth`, `findByAutor`, `findAllByOrderByIdDesc`, Specifications de `ReceitaRepositoryTest`.
- Se as queries forem **nativas/PostgreSQL**, H2 pode divergir: prefira **Testcontainers** (`org.testcontainers:postgresql` + `@ServiceConnection`), exige Docker. Caso contrário H2 (`com.h2database:h2`, escopo test) basta.
- Mockito aqui não se aplica; use `TestEntityManager` para montar os dados.
- Teste limites: virada de mês/ano, valores `null`, mês sem registros.

---

## Fase 5 – Cobertura e automação

1. Adicione o **JaCoCo** ao `pom.xml` (goals `prepare-agent` + `report`); relatório em `target/site/jacoco/index.html`.
2. Meta sugerida: Services ≥ 90 %, Controllers ≥ 80 %, geral ≥ 75 %.
3. Execução: `./mvnw test` (unit) e `./mvnw verify` (com cobertura). Rodar um teste: `./mvnw test -Dtest=DespesaServiceTest`.
4. (Opcional) GitHub Actions rodando `./mvnw verify` em cada push.
5. Separe testes lentos (Testcontainers) com `@Tag("integration")` e Surefire/Failsafe.

---

## Checklist de entrega

- [ ] Fase 0: perfil de teste e `V1ApplicationTests` corrigido
- [ ] Fase 1: `DespesaServiceTest`, `ReceitaServiceTest`, `ContabilidadeServiceTest`
- [ ] Fase 2: três `*ControllerTest`
- [ ] Fase 3: `RestExceptionHandlerTest`
- [ ] Fase 4: testes de repository
- [ ] Fase 5: JaCoCo + CI

## Boas práticas

- Um comportamento por teste; nomes descritivos.
- Não mocke o que você não é dono (entidades/DTOs: instancie de verdade).
- Evite `@SpringBootTest` para o que cabe em teste unitário/fatia (mais rápido).
- `verifyNoMoreInteractions(repo)` no fim quando fizer sentido.
- Use builders/factories para dados de teste em vez de duplicar `new DespesasEntity(...)`.
