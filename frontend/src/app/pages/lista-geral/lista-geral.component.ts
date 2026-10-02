import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { DespesaService } from '../../services/despesa.service';
import { ReceitaService } from '../../services/receita.service';
import { Despesa, CATEGORIA_LABELS, CategoriaEnum } from '../../models/despesa.model';
import { Receita, CATEGORIA_RECEITA_LABELS, CategoriaReceitaEnum } from '../../models/receita.model';
import { SITUACAO_LABELS } from '../../models/shared.model';

@Component({
  selector: 'app-lista-geral',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-header">
      <div>
        <h1>Histórico Geral</h1>
        <p class="subtitle">Visualize todas as suas movimentações de forma paginada</p>
      </div>
    </div>

    <div class="filter-bar card">
      <div class="filter-group">
        <button class="btn" [class.btn-primary]="tipo === 'despesa'" [class.btn-outline]="tipo !== 'despesa'" (click)="setTipo('despesa')">
          Despesas
        </button>
        <button class="btn" [class.btn-success]="tipo === 'receita'" [class.btn-outline]="tipo !== 'receita'" (click)="setTipo('receita')">
          Receitas
        </button>
      </div>
    </div>

    <div class="card mt-lg">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h3 style="margin: 0;">Lista Paginada ({{ tipo === 'despesa' ? 'Despesas' : 'Receitas' }})</h3>
      </div>
      
      <div class="table-wrapper">
        <table *ngIf="tipo === 'despesa'">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Descrição</th>
              <th>Autor</th>
              <th>Categoria</th>
              <th>Valor</th>
              <th>Data</th>
              <th>Situação</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let d of despesas">
              <td style="color: var(--text-muted)">#{{ d.id }}</td>
              <td style="font-weight: 600">{{ d.name }}</td>
              <td style="color: var(--text-secondary)">{{ d.descricao }}</td>
              <td><span class="badge" style="background: var(--bg); color: var(--text-secondary)">{{ d.autor || '-' }}</span></td>
              <td>{{ getCategoriaDespesaLabel(d.categoriaEnum) }}</td>
              <td style="font-weight: 600; color: #ef4444">{{ formatCurrency(d.valor) }}</td>
              <td>{{ formatDate(d.localDateTime) }}</td>
              <td>
                <span class="badge" [class.badge-success]="d.situacaoEnum === 'PAGA'" [class.badge-warning]="d.situacaoEnum === 'A_PAGAR'">
                  {{ getSituacaoLabel(d.situacaoEnum) }}
                </span>
              </td>
            </tr>
            <tr *ngIf="despesas.length === 0">
              <td colspan="8" class="text-center" style="padding: 40px; color: var(--text-muted)">Nenhuma despesa encontrada na página</td>
            </tr>
          </tbody>
        </table>

        <table *ngIf="tipo === 'receita'">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Descrição</th>
              <th>Autor</th>
              <th>Categoria</th>
              <th>Valor</th>
              <th>Data</th>
              <th>Situação</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let r of receitas">
              <td style="color: var(--text-muted)">#{{ r.id }}</td>
              <td style="font-weight: 600">{{ r.name }}</td>
              <td style="color: var(--text-secondary)">{{ r.descricao }}</td>
              <td><span class="badge" style="background: var(--bg); color: var(--text-secondary)">{{ r.autor || '-' }}</span></td>
              <td>{{ getCategoriaReceitaLabel(r.categoriaReceitaEnum) }}</td>
              <td style="font-weight: 600; color: #22c55e">{{ formatCurrency(r.valor) }}</td>
              <td>{{ formatDate(r.localDateTimeEntrada) }}</td>
              <td>
                <span class="badge" [class.badge-success]="r.situacaoEnum === 'RECEBIDO'" [class.badge-info]="r.situacaoEnum === 'A_RECEBER'">
                  {{ getSituacaoLabel(r.situacaoEnum) }}
                </span>
              </td>
            </tr>
            <tr *ngIf="receitas.length === 0">
              <td colspan="8" class="text-center" style="padding: 40px; color: var(--text-muted)">Nenhuma receita encontrada na página</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="pagination-controls" style="display: flex; justify-content: space-between; align-items: center; padding-top: 20px; margin-top: 10px; border-top: 1px solid var(--border);">
        <button class="btn btn-outline" [disabled]="page === 0" (click)="prevPage()">
          <span class="material-icons-outlined" style="font-size: 16px; margin-right: 4px;">chevron_left</span> Anterior
        </button>
        <span style="font-size: 14px; font-weight: 500; color: var(--text-secondary);">
          Página {{ page + 1 }}
        </span>
        <button class="btn btn-outline" [disabled]="!hasMore" (click)="nextPage()">
          Próxima <span class="material-icons-outlined" style="font-size: 16px; margin-left: 4px;">chevron_right</span>
        </button>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
    .page-header h1 { font-size: 28px; font-weight: 700; }
    .subtitle { color: var(--text-secondary); margin-top: 4px; }

    .filter-bar { display: flex; align-items: center; margin-bottom: 20px; padding: 16px 20px; }
    .filter-group { display: flex; gap: 12px; }

    .table-wrapper { overflow-x: auto; }
  `]
})
export class ListaGeralComponent implements OnInit {
  private despesaService = inject(DespesaService);
  private receitaService = inject(ReceitaService);

  tipo: 'despesa' | 'receita' = 'despesa';
  page = 0;
  size = 10;
  hasMore = true;

  despesas: Despesa[] = [];
  receitas: Receita[] = [];

  ngOnInit(): void {
    this.loadData();
  }

  setTipo(novoTipo: 'despesa' | 'receita') {
    if (this.tipo !== novoTipo) {
      this.tipo = novoTipo;
      this.page = 0;
      this.despesas = [];
      this.receitas = [];
      this.loadData();
    }
  }

  loadData(): void {
    if (this.tipo === 'despesa') {
      this.despesaService.listPaginated(this.page, this.size).subscribe(data => {
        this.despesas = data;
        this.hasMore = data.length === this.size;
      });
    } else {
      this.receitaService.listPaginated(this.page, this.size).subscribe(data => {
        this.receitas = data;
        this.hasMore = data.length === this.size;
      });
    }
  }

  nextPage(): void {
    if (this.hasMore) {
      this.page++;
      this.loadData();
    }
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadData();
    }
  }

  getCategoriaDespesaLabel(cat: string): string {
    return CATEGORIA_LABELS[cat as CategoriaEnum] || cat;
  }

  getCategoriaReceitaLabel(cat: string): string {
    return CATEGORIA_RECEITA_LABELS[cat as CategoriaReceitaEnum] || cat;
  }

  getSituacaoLabel(sit: string): string {
    return (SITUACAO_LABELS as any)[sit] || sit;
  }

  formatCurrency(val: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val || 0);
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString('pt-BR');
  }
}
