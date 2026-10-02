import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { catchError, of } from 'rxjs';

import { ContabilidadeService } from '../../services/contabilidade.service';
import { Despesa, CATEGORIA_LABELS, CategoriaEnum } from '../../models/despesa.model';
import { Receita, CATEGORIA_RECEITA_LABELS, CategoriaReceitaEnum } from '../../models/receita.model';
import { MESES, SITUACAO_LABELS } from '../../models/shared.model';

@Component({
  selector: 'app-contabilidade',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  template: `
    <div class="page-header">
      <div>
        <h1>Contabilidade</h1>
        <p class="subtitle">Balanço mensal de receitas e despesas</p>
      </div>
      <div class="filters">
        <select [(ngModel)]="selectedMes" (ngModelChange)="loadData()">
          <option *ngFor="let m of meses" [value]="m.value">{{ m.label }}</option>
        </select>
        <select [(ngModel)]="selectedAno" (ngModelChange)="loadData()">
          <option *ngFor="let y of anos" [value]="y">{{ y }}</option>
        </select>
      </div>
    </div>

    <!-- Summary Cards -->
    <div class="summary-row">
      <div class="summary-card green">
        <div class="card-icon"><span class="material-icons-outlined">trending_up</span></div>
        <div class="card-info">
          <span class="card-label">Total Receitas</span>
          <span class="card-value">{{ formatCurrency(totalReceitas) }}</span>
        </div>
      </div>
      <div class="summary-card red">
        <div class="card-icon"><span class="material-icons-outlined">trending_down</span></div>
        <div class="card-info">
          <span class="card-label">Total Despesas</span>
          <span class="card-value">{{ formatCurrency(totalDespesas) }}</span>
        </div>
      </div>
      <div class="summary-card" [class.positive]="saldo >= 0" [class.negative]="saldo < 0">
        <div class="card-icon"><span class="material-icons-outlined">account_balance_wallet</span></div>
        <div class="card-info">
          <span class="card-label">Saldo do Mês</span>
          <span class="card-value" [style.color]="saldo >= 0 ? '#22c55e' : '#ef4444'">
            {{ formatCurrency(saldo) }}
          </span>
        </div>
      </div>
    </div>

    <!-- Chart -->
    <div class="card chart-section">
      <h3>📊 Receitas vs Despesas por Categoria</h3>
      <div class="chart-container">
        <canvas baseChart [data]="barData" [options]="barOptions" type="bar"></canvas>
      </div>
    </div>

    <!-- Polar Area Chart -->
    <div class="card mt-lg chart-section">
      <h3>🎯 Distribuição Geral</h3>
      <div class="chart-container-sm">
        <canvas baseChart [data]="polarData" [options]="polarOptions" type="polarArea"></canvas>
      </div>
    </div>

    <!-- Tables Side by Side -->
    <div class="tables-row mt-lg">
      <div class="card">
        <h3 class="mb-md" style="color: #22c55e">💰 Receitas do Mês</h3>
        <div class="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Nome</th>
                <th>Categoria</th>
                <th>Valor</th>
                <th>Data</th>
                <th>Situação</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let r of receitas">
                <td style="font-weight: 600">{{ r.name }}</td>
                <td>{{ getReceitaCatLabel(r.categoriaReceitaEnum) }}</td>
                <td style="font-weight: 600; color: #22c55e">{{ formatCurrency(r.valor) }}</td>
                <td>{{ formatDate(r.localDateTimeEntrada) }}</td>
                <td>
                  <span class="badge" [class.badge-success]="r.situacaoEnum === 'RECEBIDO'" [class.badge-info]="r.situacaoEnum === 'A_RECEBER'">
                    {{ getSituacaoLabel(r.situacaoEnum) }}
                  </span>
                </td>
              </tr>
              <tr *ngIf="receitas.length === 0">
                <td colspan="5" class="text-center" style="padding: 30px; color: var(--text-muted)">Nenhuma receita</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card">
        <h3 class="mb-md" style="color: #ef4444">💸 Despesas do Mês</h3>
        <div class="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Nome</th>
                <th>Categoria</th>
                <th>Valor</th>
                <th>Data</th>
                <th>Situação</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let d of despesas">
                <td style="font-weight: 600">{{ d.name }}</td>
                <td>{{ getDespesaCatLabel(d.categoriaEnum) }}</td>
                <td style="font-weight: 600; color: #ef4444">{{ formatCurrency(d.valor) }}</td>
                <td>{{ formatDate(d.localDateTime) }}</td>
                <td>
                  <span class="badge" [class.badge-success]="d.situacaoEnum === 'PAGA'" [class.badge-warning]="d.situacaoEnum === 'A_PAGAR'">
                    {{ getSituacaoLabel(d.situacaoEnum) }}
                  </span>
                </td>
              </tr>
              <tr *ngIf="despesas.length === 0">
                <td colspan="5" class="text-center" style="padding: 30px; color: var(--text-muted)">Nenhuma despesa</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 28px; }
    .page-header h1 { font-size: 28px; font-weight: 700; }
    .subtitle { color: var(--text-secondary); margin-top: 4px; }
    .filters { display: flex; gap: 10px; }
    .filters select { width: 160px; }

    .summary-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 28px; }
    .summary-card {
      background: white; border-radius: var(--radius); padding: 24px;
      display: flex; align-items: center; gap: 16px; box-shadow: var(--shadow);
      transition: all 0.2s ease;
    }
    .summary-card:hover { transform: translateY(-2px); box-shadow: var(--shadow-lg); }
    .card-icon {
      width: 52px; height: 52px; border-radius: 14px;
      display: flex; align-items: center; justify-content: center;
    }
    .card-icon .material-icons-outlined { font-size: 26px; color: white; }
    .green .card-icon { background: linear-gradient(135deg, #22c55e, #16a34a); }
    .red .card-icon { background: linear-gradient(135deg, #ef4444, #dc2626); }
    .positive .card-icon { background: linear-gradient(135deg, #22c55e, #16a34a); }
    .negative .card-icon { background: linear-gradient(135deg, #ef4444, #dc2626); }
    .card-info { display: flex; flex-direction: column; }
    .card-label { font-size: 13px; color: var(--text-secondary); font-weight: 500; }
    .card-value { font-size: 22px; font-weight: 700; margin-top: 4px; }

    .chart-section h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
    .chart-container { position: relative; height: 340px; }
    .chart-container-sm { position: relative; height: 300px; max-width: 500px; margin: 0 auto; }

    .tables-row { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; }
    .table-wrapper { overflow-x: auto; }
    h3 { font-size: 16px; font-weight: 600; }

    @media (max-width: 1024px) {
      .summary-row { grid-template-columns: 1fr; }
      .tables-row { grid-template-columns: 1fr; }
    }
  `]
})
export class ContabilidadeComponent implements OnInit {
  private contabilidadeService = inject(ContabilidadeService);

  meses = MESES;
  anos = [2024, 2025, 2026, 2027];
  selectedMes = new Date().getMonth() + 1;
  selectedAno = new Date().getFullYear();

  receitas: Receita[] = [];
  despesas: Despesa[] = [];
  totalReceitas = 0;
  totalDespesas = 0;
  saldo = 0;

  barData: ChartData<'bar'> = { labels: [], datasets: [] };
  barOptions: ChartOptions<'bar'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { labels: { usePointStyle: true, font: { size: 12 } } } },
    scales: {
      y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.04)' } },
      x: { grid: { display: false } }
    }
  };

  polarData: ChartData<'polarArea'> = { labels: [], datasets: [{ data: [] }] };
  polarOptions: ChartOptions<'polarArea'> = {
    responsive: true, maintainAspectRatio: false,
    plugins: { legend: { position: 'right', labels: { usePointStyle: true, font: { size: 12 } } } }
  };

  private colors = ['#6366f1', '#22c55e', '#f59e0b', '#ef4444', '#3b82f6', '#ec4899', '#14b8a6', '#f97316'];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.contabilidadeService.getContabilidade(this.selectedAno, this.selectedMes)
      .pipe(catchError(() => of({ receita: [], despesa: [] })))
      .subscribe(data => {
        this.receitas = (data.receita || []).sort((a, b) =>
          new Date(b.localDateTimeEntrada).getTime() - new Date(a.localDateTimeEntrada).getTime()
        );
        this.despesas = (data.despesa || []).sort((a, b) =>
          new Date(b.localDateTime).getTime() - new Date(a.localDateTime).getTime()
        );

        this.totalReceitas = this.receitas.reduce((s, r) => s + r.valor, 0);
        this.totalDespesas = this.despesas.reduce((s, d) => s + d.valor, 0);
        this.saldo = this.totalReceitas - this.totalDespesas;

        this.buildCharts();
      });
  }

  buildCharts(): void {
    // Bar chart: despesas grouped by category vs receitas grouped by category
    const despesaCats = new Map<string, number>();
    this.despesas.forEach(d => {
      const label = this.getDespesaCatLabel(d.categoriaEnum);
      despesaCats.set(label, (despesaCats.get(label) || 0) + d.valor);
    });

    const receitaCats = new Map<string, number>();
    this.receitas.forEach(r => {
      const label = this.getReceitaCatLabel(r.categoriaReceitaEnum);
      receitaCats.set(label, (receitaCats.get(label) || 0) + r.valor);
    });

    const allLabels = Array.from(new Set([...despesaCats.keys(), ...receitaCats.keys()]));
    this.barData = {
      labels: allLabels,
      datasets: [
        {
          label: 'Receitas',
          data: allLabels.map(l => receitaCats.get(l) || 0),
          backgroundColor: 'rgba(34, 197, 94, 0.8)',
          borderColor: '#22c55e',
          borderWidth: 1,
          borderRadius: 6
        },
        {
          label: 'Despesas',
          data: allLabels.map(l => despesaCats.get(l) || 0),
          backgroundColor: 'rgba(239, 68, 68, 0.8)',
          borderColor: '#ef4444',
          borderWidth: 1,
          borderRadius: 6
        }
      ]
    };

    // Polar Area: combine all categories
    const allCats = new Map<string, number>();
    this.despesas.forEach(d => {
      const label = this.getDespesaCatLabel(d.categoriaEnum);
      allCats.set(label, (allCats.get(label) || 0) + d.valor);
    });
    this.receitas.forEach(r => {
      const label = this.getReceitaCatLabel(r.categoriaReceitaEnum);
      allCats.set(label, (allCats.get(label) || 0) + r.valor);
    });
    this.polarData = {
      labels: Array.from(allCats.keys()),
      datasets: [{
        data: Array.from(allCats.values()),
        backgroundColor: this.colors.slice(0, allCats.size).map(c => c + 'cc')
      }]
    };
  }

  getDespesaCatLabel(cat: string): string {
    return CATEGORIA_LABELS[cat as CategoriaEnum] || cat;
  }

  getReceitaCatLabel(cat: string): string {
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
