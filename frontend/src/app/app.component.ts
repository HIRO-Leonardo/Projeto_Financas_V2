import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="app-layout">
      <!-- Overlay (mobile) -->
      <div class="sidebar-overlay" [class.visible]="sidebarOpen" (click)="closeSidebar()"></div>

      <!-- Sidebar -->
      <aside class="sidebar" [class.open]="sidebarOpen">
        <div class="sidebar-header">
          <span class="logo-icon">💰</span>
          <span class="logo-text">Painel Financeiro</span>
          <button class="close-btn" (click)="closeSidebar()">
            <span class="material-icons-outlined">close</span>
          </button>
        </div>

        <nav class="sidebar-nav">
          <a routerLink="/dashboard" routerLinkActive="active" class="nav-item" (click)="closeSidebar()">
            <span class="material-icons-outlined">dashboard</span>
            <span>Dashboard</span>
          </a>
          <a routerLink="/minhas-despesas" routerLinkActive="active" class="nav-item" (click)="closeSidebar()">
            <span class="material-icons-outlined">trending_down</span>
            <span>Despesas</span>
          </a>
          <a routerLink="/minhas-receitas" routerLinkActive="active" class="nav-item" (click)="closeSidebar()">
            <span class="material-icons-outlined">trending_up</span>
            <span>Receitas</span>
          </a>
          <a routerLink="/minha-contabilidade" routerLinkActive="active" class="nav-item" (click)="closeSidebar()">
            <span class="material-icons-outlined">account_balance</span>
            <span>Contabilidade</span>
          </a>
          <a routerLink="/lista-geral" routerLinkActive="active" class="nav-item" (click)="closeSidebar()">
            <span class="material-icons-outlined">list_alt</span>
            <span>Lista Geral</span>
          </a>
        </nav>

        <div class="sidebar-footer">
          <span class="material-icons-outlined">calendar_today</span>
          <span class="footer-date">Sistema de Finanças v1.0</span>
        </div>
      </aside>

      <!-- Main Content -->
      <main class="main-content">
        <!-- Top bar mobile -->
        <header class="topbar">
          <button class="hamburger" (click)="toggleSidebar()">
            <span class="material-icons-outlined">menu</span>
          </button>
          <span class="topbar-title">💰 Painel Financeiro</span>
        </header>

        <div class="page-content">
          <router-outlet></router-outlet>
        </div>
      </main>
    </div>
  `,
  styles: [`
    .app-layout {
      display: flex;
      min-height: 100vh;
    }

    /* ===== Sidebar ===== */
    .sidebar {
      width: 260px;
      min-height: 100vh;
      background: linear-gradient(180deg, #1e1b4b 0%, #312e81 100%);
      display: flex;
      flex-direction: column;
      position: fixed;
      top: 0;
      left: 0;
      z-index: 200;
      transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .sidebar-header {
      padding: 28px 24px;
      display: flex;
      align-items: center;
      gap: 12px;
      border-bottom: 1px solid rgba(255,255,255,0.08);
    }

    .logo-icon { font-size: 28px; }

    .logo-text {
      font-size: 18px;
      font-weight: 700;
      color: white;
      letter-spacing: -0.02em;
    }

    .close-btn {
      display: none;
      margin-left: auto;
      background: none;
      border: none;
      color: #a5b4fc;
      cursor: pointer;
      padding: 4px;
      border-radius: 8px;
      transition: all 0.2s;
    }
    .close-btn:hover { background: rgba(255,255,255,0.1); color: white; }

    .sidebar-nav {
      flex: 1;
      padding: 16px 12px;
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .nav-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 16px;
      border-radius: 10px;
      color: #a5b4fc;
      text-decoration: none;
      font-weight: 500;
      font-size: 14px;
      transition: all 0.2s ease;
    }
    .nav-item:hover { background: rgba(255,255,255,0.08); color: white; }
    .nav-item.active {
      background: rgba(99, 102, 241, 0.3);
      color: white;
      box-shadow: 0 0 20px rgba(99, 102, 241, 0.15);
    }
    .nav-item .material-icons-outlined { font-size: 22px; }

    .sidebar-footer {
      padding: 20px 24px;
      border-top: 1px solid rgba(255,255,255,0.08);
      display: flex;
      align-items: center;
      gap: 10px;
      color: #a5b4fc;
      font-size: 12px;
    }
    .sidebar-footer .material-icons-outlined { font-size: 18px; }

    /* ===== Overlay ===== */
    .sidebar-overlay {
      display: none;
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.5);
      backdrop-filter: blur(4px);
      z-index: 150;
      opacity: 0;
      transition: opacity 0.3s ease;
    }

    /* ===== Topbar (mobile) ===== */
    .topbar {
      display: none;
      align-items: center;
      gap: 12px;
      padding: 12px 20px;
      background: white;
      border-bottom: 1px solid var(--border);
      box-shadow: 0 1px 3px rgba(0,0,0,0.05);
      position: sticky;
      top: 0;
      z-index: 50;
    }

    .hamburger {
      background: none;
      border: none;
      cursor: pointer;
      padding: 8px;
      border-radius: 10px;
      color: var(--text);
      transition: all 0.2s;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .hamburger:hover { background: var(--bg); }
    .hamburger .material-icons-outlined { font-size: 26px; }

    .topbar-title {
      font-size: 16px;
      font-weight: 700;
      color: var(--text);
    }

    /* ===== Main Content ===== */
    .main-content {
      flex: 1;
      margin-left: 260px;
      min-height: 100vh;
      background: var(--bg);
    }

    .page-content {
      padding: 32px;
    }

    /* ===== TABLET (<=1024px) ===== */
    @media (max-width: 1024px) {
      .page-content {
        padding: 24px 20px;
      }
    }

    /* ===== MOBILE (<=768px) ===== */
    @media (max-width: 768px) {
      .sidebar {
        transform: translateX(-100%);
      }
      .sidebar.open {
        transform: translateX(0);
      }

      .sidebar-overlay.visible {
        display: block;
        opacity: 1;
      }

      .close-btn {
        display: flex;
      }

      .topbar {
        display: flex;
      }

      .main-content {
        margin-left: 0;
      }

      .page-content {
        padding: 16px;
      }
    }

    /* ===== SMALL MOBILE (<=480px) ===== */
    @media (max-width: 480px) {
      .sidebar {
        width: 100%;
      }

      .page-content {
        padding: 12px;
      }

      .topbar {
        padding: 10px 16px;
      }
    }
  `]
})
export class AppComponent {
  sidebarOpen = false;

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar(): void {
    this.sidebarOpen = false;
  }
}
