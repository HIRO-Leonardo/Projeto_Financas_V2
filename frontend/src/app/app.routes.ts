import { Routes } from '@angular/router';

import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'dashboard', canActivate: [authGuard], loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
  { path: 'minhas-despesas', canActivate: [authGuard], loadComponent: () => import('./pages/despesas/despesas.component').then(m => m.DespesasComponent) },
  { path: 'minhas-receitas', canActivate: [authGuard], loadComponent: () => import('./pages/receitas/receitas.component').then(m => m.ReceitasComponent) },
  { path: 'lista-geral', canActivate: [authGuard], loadComponent: () => import('./pages/lista-geral/lista-geral.component').then(m => m.ListaGeralComponent) },
  { path: 'minha-contabilidade', canActivate: [authGuard], loadComponent: () => import('./pages/contabilidade/contabilidade.component').then(m => m.ContabilidadeComponent) },
  { path: '**', redirectTo: 'dashboard' }
];
