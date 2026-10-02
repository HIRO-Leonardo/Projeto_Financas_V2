import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { catchError, of } from 'rxjs';

import { DespesaService } from '../../services/despesa.service';
import { Despesa, DespesaDTO, CATEGORIA_LABELS, CategoriaEnum, CATEGORIAS } from '../../models/despesa.model';
import { MESES, SITUACAO_LABELS, SituacaoEnum } from '../../models/shared.model';

@Component({
  selector: 'app-despesas',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  template: `
    <div class="page-header">
      <div>
        <h1>Despesas</h1>
        <p class="subtitle">Gerencie suas despesas e gastos</p>
      </div>
      <button class="btn btn-primary" (click)="openModal()">
        <span class="material-icons-outlined">add</span>
        Nova Despesa
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
        <input type="text" placeholder="🔍 Buscar despesa..." [(ngModel)]="searchTerm" (input)="filterList()">
      </div>
    </div>

    <!-- Summary Cards -->
    <div class="summary-row">
      <div class="mini-card">
        <span class="mini-label">Total de Despesas</span>
        <span class="mini-value">{{ formatCurrency(totalDespesas) }}</span>
      </div>
      <div class="mini-card green">
        <span class="mini-label">Despesas Pagas</span>
        <span class="mini-value">{{ formatCurrency(totalPagas) }}</span>
      </div>
      <div class="mini-card orange">
        <span class="mini-label">Despesas A Pagar</span>
        <span class="mini-value">{{ formatCurrency(totalAPagar) }}</span>
      </div>
    </div>

    <!-- Charts -->
    <div class="charts-row">
      <div class="card chart-card">
        <h3>🥧 Despesas por Categoria</h3>
        <div class="chart-container">
          <canvas baseChart [data]="pieData" [options]="pieOptions" type="pie"></canvas>
        </div>
      </div>
      <div class="card chart-card">
        <h3>📊 Por Categoria (Barras)</h3>
        <div class="chart-container">
          <canvas baseChart [data]="barData" [options]="barOptions" type="bar"></canvas>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="card mt-lg">
      <h3 class="mb-md">Lista de Despesas</h3>
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
            <tr *ngFor="let d of filteredDespesas">
              <td style="font-weight: 600">{{ d.name }}</td>
              <td style="color: var(--text-secondary)">{{ d.descricao }}</td>
              <td>
                <span class="badge" style="background: var(--bg); color: var(--text-secondary)">
                  {{ d.autor || 'Não informado' }}
                </span>
              </td>
              <td>{{ getCategoriaLabel(d.categoriaEnum) }}</td>
              <td style="font-weight: 600; color: #ef4444">{{ formatCurrency(d.valor) }}</td>
              <td>{{ formatDate(d.localDateTime) }}</td>
              <td>
                <span class="badge" [class.badge-success]="d.situacaoEnum === 'PAGA'" [class.badge-warning]="d.situacaoEnum === 'A_PAGAR'">
                  {{ getSituacaoLabel(d.situacaoEnum) }}
                </span>
              </td>
              <td>
                <div class="actions">
                  <button class="icon-btn" (click)="editDespesa(d)" title="Editar">
                    <span class="material-icons-outlined">edit</span>
                  </button>
                  <button class="icon-btn danger" (click)="deleteDespesa(d.id)" title="Excluir">
                    <span class="material-icons-outlined">delete</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr *ngIf="filteredDespesas.length === 0">
              <td colspan="7" class="text-center" style="padding: 40px; color: var(--text-muted)">
                Nenhuma despesa encontrada
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal-overlay" *ngIf="showModal" (click)="closeModal()">
      <div class="modal-card" (click)="$event.stopPropagation()">
        <h2>{{ isEditing ? 'Editar Despesa' : 'Nova Despesa' }}</h2>
        <div class="form-grid">
          <div class="form-group">
            <label>Nome</label>
            <input type="text" [(ngModel)]="form.name" placeholder="Nome da despesa">
          </div>
          <div class="form-group">
            <label>Valor</label>
            <input type="number" [(ngModel)]="form.valor" placeholder="0.00" step="0.01">
          </div>
          <div class="form-group full">
            <label>Descrição</label>
            <input type="text" [(ngModel)]="form.descricao" placeholder="Descrição da despesa">
          </div>
          <div class="form-group">
            <label>Autor</label>
            <input type="text" [(ngModel)]="form.autor" placeholder="Quem fez essa despesa?">
          </div>
          <div class="form-group">
            <label>Data</label>
            <input type="datetime-local" [(ngModel)]="form.localDateTime">
          </div>
          <div class="form-group">
            <label>Categoria</label>
            <select [(ngModel)]="form.categoriaEnum">
              <option value="">Selecione</option>
              <option *ngFor="let c of categorias" [value]="c">{{ getCategoriaLabel(c) }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Situação</label>
            <select [(ngModel)]="form.situacaoEnum">
              <option value="A_PAGAR">A Pagar</option>
              <option value="PAGA">Paga</option>
            </select>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn btn-outline" (click)="closeModal()">Cancelar</button>
          <button class="btn btn-primary" (click)="saveDespesa()">
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
    .filter-group { display: flex; gap: 12px; flex: 1; }
    .filter-group select, .filter-group input { width: 180px; }
    .filter-group input[type="text"] { flex: 1; min-width: 200px; }

    .summary-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
    .mini-card {
      background: white; border-radius: var(--radius); padding: 20px; box-shadow: var(--shadow);
      display: flex; flex-direction: column; border-left: 4px solid var(--primary);
    }
    .mini-card.green { border-left-color: #22c55e; }
    .mini-card.orange { border-left-color: #f59e0b; }
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
      display: flex; align-items: center; justify-content: center;
      backdrop-filter: blur(4px);
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
export class DespesasComponent implements OnInit {
  private despesaService = inject(DespesaService);

  meses = MESES;
  anos = [2024, 2025, 2026, 2027];
  categorias = CATEGORIAS;
  selectedMes = new Date().getMonth() + 1;
  selectedAno = new Date().getFullYear();
  searchTerm = '';

  despesas: Despesa[] = [];
  filteredDespesas: Despesa[] = [];

  totalDespesas = 0;
  totalPagas = 0;
  totalAPagar = 0;

  showModal = false;
  isEditing = false;
  editingId: number | null = null;
  form: any = { name: '', descricao: '', valor: 0, localDateTime: '', categoriaEnum: '', situacaoEnum: 'A_PAGAR', autor: '' };

  pieData: ChartData<'pie'> = { labels: [], datasets: [{ data: [] }] };
  pieOptions: ChartOptions<'pie'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { position: 'right', labels: { usePointStyle: true, font: { size: 12 } } } }
  };

  barData: ChartData<'bar'> = { labels: [], datasets: [] };
  barOptions: ChartOptions<'bar'> = {
    responsive: true, maintainAspectRatio: false, indexAxis: 'y',
    plugins: { legend: { display: false } },
    scales: { x: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.04)' } }, y: { grid: { display: false } } }
  };

  private colors = ['#6366f1', '#22c55e', '#f59e0b', '#ef4444', '#3b82f6', '#ec4899'];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.despesaService.buscarPorMesAno(this.selectedMes, this.selectedAno)
      .pipe(catchError(() => of([])))
      .subscribe(data => {
        this.despesas = data;
        this.filterList();
        this.calcSummary();
      });

    this.despesaService.relatorioMensalCategoria(this.selectedMes, this.selectedAno)
      .pipe(catchError(() => of([])))
      .subscribe(data => {
        const labels = data.map(d => this.getCategoriaLabel(d.categoria));
        const values = data.map(d => d.valor);
        this.pieData = {
          labels,
          datasets: [{ data: values, backgroundColor: this.colors.slice(0, values.length), borderWidth: 2, borderColor: '#fff' }]
        };
        this.barData = {
          labels,
          datasets: [{ data: values, backgroundColor: this.colors.slice(0, values.length), borderRadius: 6 }]
        };
      });
  }

  filterList(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredDespesas = this.despesas.filter(d =>
      d.name.toLowerCase().includes(term) || d.descricao.toLowerCase().includes(term) || (d.autor && d.autor.toLowerCase().includes(term))
    );
  }

  calcSummary(): void {
    this.totalDespesas = this.despesas.reduce((sum, d) => sum + d.valor, 0);
    this.totalPagas = this.despesas.filter(d => d.situacaoEnum === 'PAGA').reduce((sum, d) => sum + d.valor, 0);
    this.totalAPagar = this.despesas.filter(d => d.situacaoEnum === 'A_PAGAR').reduce((sum, d) => sum + d.valor, 0);
  }

  openModal(): void {
    this.isEditing = false;
    this.editingId = null;
    this.form = { name: '', descricao: '', valor: 0, localDateTime: '', categoriaEnum: '', situacaoEnum: 'A_PAGAR', autor: '' };
    this.showModal = true;
  }

  editDespesa(d: Despesa): void {
    this.isEditing = true;
    this.editingId = d.id;
    this.form = {
      name: d.name,
      descricao: d.descricao,
      valor: d.valor,
      localDateTime: d.localDateTime ? d.localDateTime.substring(0, 16) : '',
      categoriaEnum: d.categoriaEnum,
      situacaoEnum: d.situacaoEnum,
      autor: d.autor || ''
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveDespesa(): void {
    const dto: DespesaDTO = {
      name: this.form.name,
      descricao: this.form.descricao,
      valor: this.form.valor,
      localDateTime: this.form.localDateTime,
      categoriaEnum: this.form.categoriaEnum,
      situacaoEnum: this.form.situacaoEnum,
      autor: this.form.autor
    };

    const obs = this.isEditing && this.editingId
      ? this.despesaService.update(this.editingId, dto)
      : this.despesaService.create(dto);

    obs.subscribe(() => {
      this.closeModal();
      this.loadData();
    });
  }

  deleteDespesa(id: number): void {
    if (confirm('Deseja realmente excluir esta despesa?')) {
      this.despesaService.delete(id).subscribe(() => this.loadData());
    }
  }

  getCategoriaLabel(cat: string): string {
    return CATEGORIA_LABELS[cat as CategoriaEnum] || cat;
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
