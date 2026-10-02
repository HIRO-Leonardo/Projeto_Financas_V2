import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
  { path: 'minhas-despesas', loadComponent: () => import('./pages/despesas/despesas.component').then(m => m.DespesasComponent) },
  { path: 'minhas-receitas', loadComponent: () => import('./pages/receitas/receitas.component').then(m => m.ReceitasComponent) },
  { path: 'lista-geral', loadComponent: () => import('./pages/lista-geral/lista-geral.component').then(m => m.ListaGeralComponent) },
  { path: 'minha-contabilidade', loadComponent: () => import('./pages/contabilidade/contabilidade.component').then(m => m.ContabilidadeComponent) }
];
