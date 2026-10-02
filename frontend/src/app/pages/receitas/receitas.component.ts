import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { catchError, of } from 'rxjs';

import { ReceitaService } from '../../services/receita.service';
import { Receita, ReceitaDTO, CATEGORIA_RECEITA_LABELS, CategoriaReceitaEnum, CATEGORIAS_RECEITA } from '../../models/receita.model';
import { MESES, SITUACAO_LABELS, SituacaoEnum } from '../../models/shared.model';

@Component({
  selector: 'app-receitas',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  template: `
    <div class="page-header">
      <div>
        <h1>Receitas</h1>
        <p class="subtitle">Gerencie suas receitas e rendimentos</p>
      </div>
      <button class="btn btn-success" (click)="openModal()">
        <span class="material-icons-outlined">add</span>
        Nova Receita
      </button>
    </div>

    <!-- Filters -->
    <div class="filter-bar card">
      <div class="filter-group">
        <select [(ngModel)]="selectedMes" (ngModelChange)="loadData()">
          <option *ngFor="let m of meses" [value]="m.value">{{ m.label }}</option>
        </select>
        <select [(ngModel)]="selectedAno" (ngModelChange)="loadData()">
          <option *ngFor="let y of anos" [value]="y">{{ y }}</option>
        </select>
        <select [(ngModel)]="filterCategoria" (ngModelChange)="filterList()">
          <option value="">Todas as Categorias</option>
          <option *ngFor="let c of categoriasReceita" [value]="c">{{ getCategoriaLabel(c) }}</option>
        </select>
        <select [(ngModel)]="filterSituacao" (ngModelChange)="filterList()">
          <option value="">Todas as Situações</option>
          <option value="RECEBIDO">Recebido</option>
          <option value="A_RECEBER">A Receber</option>
        </select>
      </div>
    </div>

    <!-- Summary Cards -->
    <div class="summary-row">
      <div class="mini-card">
        <span class="mini-label">Total Receitas</span>
        <span class="mini-value">{{ formatCurrency(totalReceitas) }}</span>
      </div>
      <div class="mini-card green">
        <span class="mini-label">Recebido</span>
        <span class="mini-value">{{ formatCurrency(totalRecebido) }}</span>
      </div>
      <div class="mini-card blue">
        <span class="mini-label">A Receber</span>
        <span class="mini-value">{{ formatCurrency(totalAReceber) }}</span>
      </div>
    </div>

    <!-- Charts -->
    <div class="charts-row">
      <div class="card chart-card">
        <h3>🍩 Receitas por Categoria</h3>
        <div class="chart-container">
          <canvas baseChart [data]="doughnutData" [options]="doughnutOptions" type="doughnut"></canvas>
        </div>
      </div>
      <div class="card chart-card">
        <h3>📊 Receitas por Situação</h3>
        <div class="chart-container">
          <canvas baseChart [data]="barData" [options]="barOptions" type="bar"></canvas>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="card mt-lg">
      <h3 class="mb-md">Lista de Receitas</h3>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Descrição</th>
              <th>Autor</th>
              <th>Categoria</th>
              <th>Valor</th>
              <th>Data</th>
              <th>Situação</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let r of filteredReceitas">
              <td style="font-weight: 600">{{ r.name }}</td>
              <td style="color: var(--text-secondary)">{{ r.descricao }}</td>
              <td>
                <span class="badge" style="background: var(--bg); color: var(--text-secondary)">
                  {{ r.autor || 'Não informado' }}
                </span>
              </td>
              <td>{{ getCategoriaLabel(r.categoriaReceitaEnum) }}</td>
              <td style="font-weight: 600; color: #22c55e">{{ formatCurrency(r.valor) }}</td>
              <td>{{ formatDate(r.localDateTimeEntrada) }}</td>
              <td>
                <span class="badge" [class.badge-success]="r.situacaoEnum === 'RECEBIDO'" [class.badge-info]="r.situacaoEnum === 'A_RECEBER'">
                  {{ getSituacaoLabel(r.situacaoEnum) }}
                </span>
              </td>
              <td>
                <div class="actions">
                  <button class="icon-btn" (click)="editReceita(r)" title="Editar">
                    <span class="material-icons-outlined">edit</span>
                  </button>
                  <button class="icon-btn danger" (click)="deleteReceita(r.id)" title="Excluir">
                    <span class="material-icons-outlined">delete</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr *ngIf="filteredReceitas.length === 0">
              <td colspan="7" class="text-center" style="padding: 40px; color: var(--text-muted)">
                Nenhuma receita encontrada
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal-overlay" *ngIf="showModal" (click)="closeModal()">
      <div class="modal-card" (click)="$event.stopPropagation()">
        <h2>{{ isEditing ? 'Editar Receita' : 'Nova Receita' }}</h2>
        <div class="form-grid">
          <div class="form-group">
            <label>Nome</label>
            <input type="text" [(ngModel)]="form.name" placeholder="Nome da receita">
          </div>
          <div class="form-group">
            <label>Valor</label>
            <input type="number" [(ngModel)]="form.valor" placeholder="0.00" step="0.01">
          </div>
          <div class="form-group full">
            <label>Descrição</label>
            <input type="text" [(ngModel)]="form.descricao" placeholder="Descrição da receita">
          </div>
          <div class="form-group">
            <label>Autor</label>
            <input type="text" [(ngModel)]="form.autor" placeholder="Autor da receita">
          </div>
          <div class="form-group">
            <label>Data</label>
            <input type="datetime-local" [(ngModel)]="form.localDateTimeEntrada">
          </div>
          <div class="form-group">
            <label>Categoria</label>
            <select [(ngModel)]="form.categoriaReceitaEnum">
              <option value="">Selecione</option>
              <option *ngFor="let c of categoriasReceita" [value]="c">{{ getCategoriaLabel(c) }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Situação</label>
            <select [(ngModel)]="form.situacaoEnum">
              <option value="A_RECEBER">A Receber</option>
              <option value="RECEBIDO">Recebido</option>
            </select>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn btn-outline" (click)="closeModal()">Cancelar</button>
          <button class="btn btn-success" (click)="saveReceita()">
            {{ isEditing ? 'Atualizar' : 'Cadastrar' }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
    .page-header h1 { font-size: 28px; font-weight: 700; }
    .subtitle { color: var(--text-secondary); margin-top: 4px; }

    .filter-bar { display: flex; align-items: center; margin-bottom: 20px; padding: 16px 20px; }
    .filter-group { display: flex; gap: 12px; flex: 1; flex-wrap: wrap; }
    .filter-group select { width: 180px; }

    .summary-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
    .mini-card {
      background: white; border-radius: var(--radius); padding: 20px; box-shadow: var(--shadow);
      display: flex; flex-direction: column; border-left: 4px solid var(--primary);
    }
    .mini-card.green { border-left-color: #22c55e; }
    .mini-card.blue { border-left-color: #3b82f6; }
    .mini-label { font-size: 13px; color: var(--text-secondary); font-weight: 500; }
    .mini-value { font-size: 20px; font-weight: 700; margin-top: 4px; }

    .charts-row { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; }
    .chart-card h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
    .chart-container { position: relative; height: 280px; }

    .actions { display: flex; gap: 6px; }
    .icon-btn {
      width: 34px; height: 34px; border-radius: 8px; display: flex; align-items: center;
      justify-content: center; background: #f1f5f9; border: none; cursor: pointer; transition: all 0.2s;
    }
    .icon-btn:hover { background: #e2e8f0; }
    .icon-btn .material-icons-outlined { font-size: 18px; color: var(--text-secondary); }
    .icon-btn.danger:hover { background: #fee2e2; }
    .icon-btn.danger:hover .material-icons-outlined { color: #ef4444; }

    .modal-overlay {
      position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 1000;
      display: flex; align-items: center; justify-content: center; backdrop-filter: blur(4px);
    }
    .modal-card {
      background: white; border-radius: var(--radius); padding: 32px; width: 560px;
      max-width: 90vw; box-shadow: var(--shadow-lg);
    }
    .modal-card h2 { font-size: 20px; font-weight: 700; margin-bottom: 24px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .form-group.full { grid-column: span 2; }
    .modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; }

    .table-wrapper { overflow-x: auto; }
    h3 { font-size: 16px; font-weight: 600; }

    @media (max-width: 768px) {
      .summary-row { grid-template-columns: 1fr; }
      .charts-row { grid-template-columns: 1fr; }
      .form-grid { grid-template-columns: 1fr; }
      .form-group.full { grid-column: span 1; }
    }
  `]
})
export class ReceitasComponent implements OnInit {
  private receitaService = inject(ReceitaService);

  meses = MESES;
  anos = [2024, 2025, 2026, 2027];
  categoriasReceita = CATEGORIAS_RECEITA;
  selectedMes = new Date().getMonth() + 1;
  selectedAno = new Date().getFullYear();
  filterCategoria = '';
  filterSituacao = '';

  receitas: Receita[] = [];
  filteredReceitas: Receita[] = [];

  totalReceitas = 0;
  totalRecebido = 0;
  totalAReceber = 0;

  showModal = false;
  isEditing = false;
  editingId: number | null = null;
  form: any = { name: '', descricao: '', valor: 0, localDateTimeEntrada: '', categoriaReceitaEnum: '', situacaoEnum: 'A_RECEBER', autor: '' };

  doughnutData: ChartData<'doughnut'> = { labels: [], datasets: [{ data: [] }] };
  doughnutOptions: ChartOptions<'doughnut'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { position: 'right', labels: { usePointStyle: true, font: { size: 12 } } } }
  };

  barData: ChartData<'bar'> = { labels: [], datasets: [] };
  barOptions: ChartOptions<'bar'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.04)' } }, x: { grid: { display: false } } }
  };

  private colors = ['#6366f1', '#22c55e', '#f59e0b', '#ef4444', '#3b82f6', '#ec4899', '#14b8a6', '#f97316'];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.receitaService.buscarPorMesAno(this.selectedMes, this.selectedAno)
      .pipe(catchError(() => of([])))
      .subscribe(data => {
        this.receitas = data;
        this.filterList();
        this.calcSummary();
        this.buildCharts();
      });
  }

  filterList(): void {
    this.filteredReceitas = this.receitas.filter(r => {
      const catMatch = !this.filterCategoria || r.categoriaReceitaEnum === this.filterCategoria;
      const sitMatch = !this.filterSituacao || r.situacaoEnum === this.filterSituacao;
      // Add text filter if we add searchTerm later, or just return basic logic
      return catMatch && sitMatch;
    });
  }

  calcSummary(): void {
    this.totalReceitas = this.receitas.reduce((sum, r) => sum + r.valor, 0);
    this.totalRecebido = this.receitas.filter(r => r.situacaoEnum === 'RECEBIDO').reduce((s, r) => s + r.valor, 0);
    this.totalAReceber = this.receitas.filter(r => r.situacaoEnum === 'A_RECEBER').reduce((s, r) => s + r.valor, 0);
  }

  buildCharts(): void {
    // Doughnut by category
    const grouped = new Map<string, number>();
    this.receitas.forEach(r => {
      grouped.set(r.categoriaReceitaEnum, (grouped.get(r.categoriaReceitaEnum) || 0) + r.valor);
    });
    const catLabels = Array.from(grouped.keys()).map(k => this.getCategoriaLabel(k));
    const catValues = Array.from(grouped.values());
    this.doughnutData = {
      labels: catLabels,
      datasets: [{ data: catValues, backgroundColor: this.colors.slice(0, catValues.length), borderWidth: 2, borderColor: '#fff' }]
    };

    // Bar by situação
    this.barData = {
      labels: ['Recebido', 'A Receber'],
      datasets: [{
        data: [this.totalRecebido, this.totalAReceber],
        backgroundColor: ['rgba(34, 197, 94, 0.8)', 'rgba(59, 130, 246, 0.8)'],
        borderRadius: 8
      }]
    };
  }

  openModal(): void {
    this.isEditing = false;
    this.editingId = null;
    this.form = { name: '', descricao: '', valor: 0, localDateTimeEntrada: '', categoriaReceitaEnum: '', situacaoEnum: 'A_RECEBER', autor: '' };
    this.showModal = true;
  }

  editReceita(r: Receita): void {
    this.isEditing = true;
    this.editingId = r.id;
    this.form = {
      name: r.name,
      descricao: r.descricao,
      valor: r.valor,
      localDateTimeEntrada: r.localDateTimeEntrada ? r.localDateTimeEntrada.substring(0, 16) : '',
      categoriaReceitaEnum: r.categoriaReceitaEnum,
      situacaoEnum: r.situacaoEnum,
      autor: r.autor || ''
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveReceita(): void {
    const dto: ReceitaDTO = {
      name: this.form.name,
      valor: this.form.valor,
      descricao: this.form.descricao,
      categoriaReceitaEnum: this.form.categoriaReceitaEnum,
      localDateTimeEntrada: this.form.localDateTimeEntrada,
      situacaoEnum: this.form.situacaoEnum,
      autor: this.form.autor
    };

    const obs = this.isEditing && this.editingId
      ? this.receitaService.update(this.editingId, dto)
      : this.receitaService.create(dto);

    obs.subscribe(() => {
      this.closeModal();
      this.loadData();
    });
  }

  deleteReceita(id: number): void {
    if (confirm('Deseja realmente excluir esta receita?')) {
      this.receitaService.delete(id).subscribe(() => this.loadData());
    }
  }

  getCategoriaLabel(cat: string): string {
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
