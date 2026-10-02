import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { forkJoin, of, catchError } from 'rxjs';

import { DespesaService } from '../../services/despesa.service';
import { ReceitaService } from '../../services/receita.service';
import { ContabilidadeService } from '../../services/contabilidade.service';
import { Despesa, CATEGORIA_LABELS, CategoriaEnum } from '../../models/despesa.model';
import { Receita, CATEGORIA_RECEITA_LABELS, CategoriaReceitaEnum } from '../../models/receita.model';
import { MESES, SITUACAO_LABELS } from '../../models/shared.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  template: `
    <div class="page-header">
      <div>
        <h1>Dashboard</h1>
        <p class="subtitle">Visão geral das suas finanças</p>
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
    <div class="summary-cards">
      <div class="summary-card green">
        <div class="card-icon"><span class="material-icons-outlined">trending_up</span></div>
        <div class="card-info">
          <span class="card-label">Total Receitas</span>
          <span class="card-value">{{ formatCurrency(totalRecebido) }}</span>
        </div>
      </div>
      <div class="summary-card red">
        <div class="card-icon"><span class="material-icons-outlined">trending_down</span></div>
        <div class="card-info">
          <span class="card-label">Total Despesas</span>
          <span class="card-value">{{ formatCurrency(totalPago) }}</span>
        </div>
      </div>
      <div class="summary-card blue">
        <div class="card-icon"><span class="material-icons-outlined">schedule</span></div>
        <div class="card-info">
          <span class="card-label">A Receber</span>
          <span class="card-value">{{ formatCurrency(totalAReceber) }}</span>
        </div>
      </div>
      <div class="summary-card orange">
        <div class="card-icon"><span class="material-icons-outlined">payment</span></div>
        <div class="card-info">
          <span class="card-label">A Pagar</span>
          <span class="card-value">{{ formatCurrency(totalAPagar) }}</span>
        </div>
      </div>
    </div>

    <!-- Charts Grid -->
    <div class="charts-grid">
      <div class="card chart-card">
        <h3>🥧 Despesas por Categoria</h3>
        <div class="chart-container">
          <canvas baseChart
            [data]="pieChartData"
            [options]="pieChartOptions"
            type="pie">
          </canvas>
        </div>
      </div>

      <div class="card chart-card">
        <h3>🍩 Receitas por Categoria</h3>
        <div class="chart-container">
          <canvas baseChart
            [data]="doughnutChartData"
            [options]="doughnutChartOptions"
            type="doughnut">
          </canvas>
        </div>
      </div>

      <div class="card chart-card">
        <h3>📊 Receitas vs Despesas</h3>
        <div class="chart-container">
          <canvas baseChart
            [data]="barChartData"
            [options]="barChartOptions"
            type="bar">
          </canvas>
        </div>
      </div>

      <div class="card chart-card">
        <h3>📈 Evolução do Saldo</h3>
        <div class="chart-container">
          <canvas baseChart
            [data]="lineChartData"
            [options]="lineChartOptions"
            type="line">
          </canvas>
        </div>
      </div>
    </div>

    <!-- Recent Transactions -->
    <div class="card mt-lg">
      <h3 class="mb-md">📋 Transações Recentes</h3>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Tipo</th>
              <th>Nome</th>
              <th>Categoria</th>
              <th>Valor</th>
              <th>Data</th>
              <th>Situação</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let t of recentTransactions">
              <td>
                <span class="badge" [class.badge-success]="t.tipo === 'Receita'" [class.badge-danger]="t.tipo === 'Despesa'">
                  {{ t.tipo }}
                </span>
              </td>
              <td>{{ t.name }}</td>
              <td>{{ t.categoriaLabel }}</td>
              <td [style.color]="t.tipo === 'Receita' ? '#22c55e' : '#ef4444'" style="font-weight: 600">
                {{ t.tipo === 'Receita' ? '+' : '-' }} {{ formatCurrency(t.valor) }}
              </td>
              <td>{{ formatDate(t.data) }}</td>
              <td>
                <span class="badge"
                  [class.badge-success]="t.situacao === 'PAGA' || t.situacao === 'RECEBIDO'"
                  [class.badge-warning]="t.situacao === 'A_PAGAR'"
                  [class.badge-info]="t.situacao === 'A_RECEBER'">
                  {{ getSituacaoLabel(t.situacao) }}
                </span>
              </td>
            </tr>
            <tr *ngIf="recentTransactions.length === 0">
              <td colspan="6" class="text-center" style="padding: 40px; color: var(--text-muted)">
                Nenhuma transação encontrada para este período
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [`
    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 28px;
    }
    .page-header h1 {
      font-size: 28px;
      font-weight: 700;
      color: var(--text);
    }
    .subtitle {
      color: var(--text-secondary);
      margin-top: 4px;
    }
    .filters {
      display: flex;
      gap: 10px;
    }
    .filters select {
      width: 160px;
      padding: 10px 14px;
      border: 1px solid var(--border);
      border-radius: var(--radius-sm);
      background: white;
      font-size: 14px;
      cursor: pointer;
    }

    .summary-cards {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 20px;
      margin-bottom: 28px;
    }
    .summary-card {
      background: white;
      border-radius: var(--radius);
      padding: 24px;
      display: flex;
      align-items: center;
      gap: 16px;
      box-shadow: var(--shadow);
      transition: all 0.2s ease;
    }
    .summary-card:hover {
      transform: translateY(-2px);
      box-shadow: var(--shadow-lg);
    }
    .card-icon {
      width: 52px;
      height: 52px;
      border-radius: 14px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .card-icon .material-icons-outlined { font-size: 26px; color: white; }
    .green .card-icon { background: linear-gradient(135deg, #22c55e, #16a34a); }
    .red .card-icon { background: linear-gradient(135deg, #ef4444, #dc2626); }
    .blue .card-icon { background: linear-gradient(135deg, #3b82f6, #2563eb); }
    .orange .card-icon { background: linear-gradient(135deg, #f59e0b, #d97706); }
    .card-info { display: flex; flex-direction: column; }
    .card-label { font-size: 13px; color: var(--text-secondary); font-weight: 500; }
    .card-value { font-size: 22px; font-weight: 700; color: var(--text); margin-top: 4px; }

    .charts-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 24px;
    }
    .chart-card h3 {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 16px;
      color: var(--text);
    }
    .chart-container {
      position: relative;
      height: 300px;
    }

    .table-wrapper {
      overflow-x: auto;
    }

    h3 { font-size: 16px; font-weight: 600; }

    @media (max-width: 1024px) {
      .summary-cards { grid-template-columns: repeat(2, 1fr); }
      .charts-grid { grid-template-columns: 1fr; }
    }
    @media (max-width: 640px) {
      .summary-cards { grid-template-columns: 1fr; }
      .page-header { flex-direction: column; gap: 16px; }
    }
  `]
})
export class DashboardComponent implements OnInit {
  private despesaService = inject(DespesaService);
  private receitaService = inject(ReceitaService);
  private contabilidadeService = inject(ContabilidadeService);

  meses = MESES;
  anos = [2024, 2025, 2026, 2027];
  selectedMes = new Date().getMonth() + 1;
  selectedAno = new Date().getFullYear();

  totalRecebido = 0;
  totalPago = 0;
  totalAReceber = 0;
  totalAPagar = 0;

  recentTransactions: any[] = [];

  // Pie Chart - Despesas por Categoria
  pieChartData: ChartData<'pie'> = { labels: [], datasets: [{ data: [] }] };
  pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'right', labels: { padding: 16, usePointStyle: true, font: { size: 12 } } }
    }
  };

  // Doughnut Chart - Receitas por Categoria
  doughnutChartData: ChartData<'doughnut'> = { labels: [], datasets: [{ data: [] }] };
  doughnutChartOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'right', labels: { padding: 16, usePointStyle: true, font: { size: 12 } } }
    }
  };

  // Bar Chart - Receitas vs Despesas
  barChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  barChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { labels: { usePointStyle: true, font: { size: 12 } } }
    },
    scales: {
      y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.04)' } },
      x: { grid: { display: false } }
    }
  };

  // Line Chart - Evolução
  lineChartData: ChartData<'line'> = { labels: [], datasets: [] };
  lineChartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { labels: { usePointStyle: true, font: { size: 12 } } }
    },
    scales: {
      y: { grid: { color: 'rgba(0,0,0,0.04)' } },
      x: { grid: { display: false } }
    },
    elements: {
      line: { tension: 0.4 }
    }
  };

  private readonly chartColors = [
    '#6366f1', '#22c55e', '#f59e0b', '#ef4444', '#3b82f6', '#ec4899',
    '#14b8a6', '#f97316', '#8b5cf6', '#06b6d4'
  ];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loadSummary();
    this.loadPieChart();
    this.loadDoughnutChart();
    this.loadBarAndLineCharts();
    this.loadRecentTransactions();
  }

  private loadSummary(): void {
    forkJoin({
      recebido: this.receitaService.getSaldoBySituacao('RECEBIDO').pipe(catchError(() => of(0))),
      aReceber: this.receitaService.getSaldoBySituacao('A_RECEBER').pipe(catchError(() => of(0))),
      pago: this.despesaService.getSaldoBySituacao('PAGA').pipe(catchError(() => of(0))),
      aPagar: this.despesaService.getSaldoBySituacao('A_PAGAR').pipe(catchError(() => of(0)))
    }).subscribe(result => {
      this.totalRecebido = result.recebido || 0;
      this.totalAReceber = result.aReceber || 0;
      this.totalPago = result.pago || 0;
      this.totalAPagar = result.aPagar || 0;
    });
  }

  private loadPieChart(): void {
    this.despesaService.relatorioMensalCategoria(this.selectedMes, this.selectedAno)
      .pipe(catchError(() => of([])))
      .subscribe(data => {
        const labels = data.map(d => CATEGORIA_LABELS[d.categoria as CategoriaEnum] || d.categoria);
        const values = data.map(d => d.valor);
        this.pieChartData = {
          labels,
          datasets: [{
            data: values,
            backgroundColor: this.chartColors.slice(0, values.length),
            borderWidth: 2,
            borderColor: '#ffffff'
          }]
        };
      });
  }

  private loadDoughnutChart(): void {
    this.receitaService.buscarPorMesAno(this.selectedMes, this.selectedAno)
      .pipe(catchError(() => of([])))
      .subscribe(receitas => {
        const grouped = new Map<string, number>();
        receitas.forEach(r => {
          const cat = r.categoriaReceitaEnum;
          grouped.set(cat, (grouped.get(cat) || 0) + r.valor);
        });
        const labels = Array.from(grouped.keys()).map(k => CATEGORIA_RECEITA_LABELS[k as CategoriaReceitaEnum] || k);
        const values = Array.from(grouped.values());
        this.doughnutChartData = {
          labels,
          datasets: [{
            data: values,
            backgroundColor: this.chartColors.slice(0, values.length),
            borderWidth: 2,
            borderColor: '#ffffff'
          }]
        };
      });
  }

  private loadBarAndLineCharts(): void {
    const monthLabels: string[] = [];
    const receitaValues: number[] = [];
    const despesaValues: number[] = [];
    const saldoValues: number[] = [];
    const requests: any[] = [];

    for (let i = 5; i >= 0; i--) {
      let m = this.selectedMes - i;
      let y = this.selectedAno;
      while (m <= 0) { m += 12; y--; }
      monthLabels.push(MESES[m - 1].label.substring(0, 3));
      requests.push(
        this.contabilidadeService.getContabilidade(y, m).pipe(catchError(() => of({ receita: [], despesa: [] })))
      );
    }

    forkJoin(requests).subscribe((results: any[]) => {
      results.forEach((data: any) => {
        const totalR = (data.receita || []).reduce((sum: number, r: any) => sum + (r.valor || 0), 0);
        const totalD = (data.despesa || []).reduce((sum: number, d: any) => sum + (d.valor || 0), 0);
        receitaValues.push(totalR);
        despesaValues.push(totalD);
        saldoValues.push(totalR - totalD);
      });

      this.barChartData = {
        labels: monthLabels,
        datasets: [
          {
            label: 'Receitas',
            data: receitaValues,
            backgroundColor: 'rgba(34, 197, 94, 0.8)',
            borderColor: '#22c55e',
            borderWidth: 1,
            borderRadius: 6
          },
          {
            label: 'Despesas',
            data: despesaValues,
            backgroundColor: 'rgba(239, 68, 68, 0.8)',
            borderColor: '#ef4444',
            borderWidth: 1,
            borderRadius: 6
          }
        ]
      };

      this.lineChartData = {
        labels: monthLabels,
        datasets: [{
          label: 'Saldo Líquido',
          data: saldoValues,
          borderColor: '#6366f1',
          backgroundColor: 'rgba(99, 102, 241, 0.1)',
          fill: true,
          pointBackgroundColor: '#6366f1',
          pointBorderColor: '#ffffff',
          pointBorderWidth: 2,
          pointRadius: 5
        }]
      };
    });
  }

  private loadRecentTransactions(): void {
    forkJoin({
      despesas: this.despesaService.buscarPorMesAno(this.selectedMes, this.selectedAno).pipe(catchError(() => of([]))),
      receitas: this.receitaService.buscarPorMesAno(this.selectedMes, this.selectedAno).pipe(catchError(() => of([])))
    }).subscribe(({ despesas, receitas }) => {
      const transactions: any[] = [];
      despesas.forEach(d => transactions.push({
        tipo: 'Despesa', name: d.name, valor: d.valor,
        categoriaLabel: CATEGORIA_LABELS[d.categoriaEnum] || d.categoriaEnum,
        data: d.localDateTime, situacao: d.situacaoEnum
      }));
      receitas.forEach(r => transactions.push({
        tipo: 'Receita', name: r.name, valor: r.valor,
        categoriaLabel: CATEGORIA_RECEITA_LABELS[r.categoriaReceitaEnum] || r.categoriaReceitaEnum,
        data: r.localDateTimeEntrada, situacao: r.situacaoEnum
      }));
      transactions.sort((a, b) => new Date(b.data).getTime() - new Date(a.data).getTime());
      this.recentTransactions = transactions.slice(0, 10);
    });
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0);
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    return d.toLocaleDateString('pt-BR');
  }

  getSituacaoLabel(situacao: string): string {
    return (SITUACAO_LABELS as any)[situacao] || situacao;
  }
}
