# Roadmap: Adicionar Autor às Despesas

Este é o passo a passo para você mesmo implementar a funcionalidade de registrar quem foi o "Autor" (responsável) de cada despesa (e se quiser, receita também) no seu sistema Spring Boot + Angular.

## 1. Back-end (Java / Spring Boot)

### 1.1 Entidade (`DespesasEntity.java`)
- Adicione o novo atributo à sua entidade. 
- *Dica:* Por enquanto, pode ser uma `String`. No futuro, você pode mudar para um relacionamento `@ManyToOne` com uma classe `UsuarioEntity`.
```java
@Column(name = "autor")
private String autor;

// Não esqueça de gerar os Getters, Setters ou atualizar o construtor!
```

### 1.2 DTO (`DespesaDTO.java`)
- Atualize o DTO que recebe os dados do Angular para incluir o novo campo.
```java
@NotBlank(message = "O autor é obrigatório")
private String autor;
```
- Atualize também a conversão de DTO para Entidade que acontece no seu `DespesaService`.

### 1.3 Banco de Dados (PostgreSQL)
- Como você usa `spring.jpa.hibernate.ddl-auto=update` no seu `application-dev.properties`, o Hibernate vai criar a coluna `autor` automaticamente na tabela `despesas_entity` ao rodar a aplicação.
- *Atenção:* As despesas já cadastradas ficarão com esse campo vazio (`null`).

### 1.4 Repositório (Opcional - `DespesasRepository.java`)
- Se quiser criar gráficos ou filtros por pessoa futuramente, adicione queries específicas:
```java
List<DespesasEntity> findByAutor(String autor);
```

---

## 2. Front-end (Angular)

### 2.1 Modelos (`despesa.model.ts`)
- Atualize a interface e os DTOs do front-end para o TypeScript reconhecer o novo campo.
```typescript
export interface Despesa {
  // ... outros campos
  autor: string;
}

export interface DespesaDTO {
  // ... outros campos
  autor: string;
}
```

### 2.2 Modal de Cadastro (`despesas.component.ts` - Template)
- No formulário onde a despesa é criada, adicione um novo campo (`form-group`).
```html
<div class="form-group">
  <label>Autor</label>
  <input type="text" [(ngModel)]="form.autor" placeholder="Quem fez essa despesa?">
</div>
<!-- Alternativa: Você pode usar um <select> se forem sempre as mesmas pessoas na casa -->
```

### 2.3 Lógica do Componente (`despesas.component.ts` - TypeScript)
- Atualize a inicialização do formulário no `openModal()` e as variáveis da classe:
```typescript
form: any = { 
  name: '', descricao: '', valor: 0, 
  localDateTime: '', categoriaEnum: '', situacaoEnum: 'A_PAGAR',
  autor: '' // <--- Novo campo inicializado
};
```
- Faça o mapeamento correto dentro dos métodos `editDespesa(d: Despesa)` e `saveDespesa()`.

### 2.4 Tabela de Listagem
- Adicione a coluna visualmente no HTML da tabela:
```html
<!-- No thead -->
<th>Autor</th>

<!-- No tbody -->
<td>
  <span class="badge" style="background: var(--bg); color: var(--text-secondary)">
    {{ d.autor || 'Não informado' }}
  </span>
</td>
```

---

## 3. Melhorias Futuras 🚀
Depois que essa versão básica com "String" estiver funcionando, você pode evoluir para:
1. **Filtros no Front-end:** Adicionar um `select` na barra superior ao lado dos Meses para ver "Quanto o João gastou esse mês?".
2. **Gráficos:** Um novo gráfico de pizza: *"Despesas por Autor"*.
3. **Autenticação:** Adicionar Login (Spring Security + JWT) e pegar o nome do "autor" automaticamente baseado no usuário que está logado, sem precisar digitar no formulário.
