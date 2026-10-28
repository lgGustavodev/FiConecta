import { Routes } from '@angular/router';
import { authGuard, tipoUsuarioGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./shared/components/login-component/login-component').then(m => m.LoginComponent)
  },
  {
    path: 'cadastro',
    loadComponent: () =>
      import('./shared/components/cadastro-component/cadastro-component').then(m => m.CadastroComponent)
  },
  {
    path: 'prestador/dashboard',
    canActivate: [authGuard, tipoUsuarioGuard('PRESTADOR')],
    loadComponent: () =>
      import('./features/prestador-dashboard/prestador-dashboard.component').then(m => m.PrestadorDashboardComponent)
  },
  {
    path: 'empresa/dashboard',
    canActivate: [authGuard, tipoUsuarioGuard('EMPRESA')],
    loadComponent: () =>
      import('./features/empresa-dashboard/empresa-dashboard.component').then(m => m.EmpresaDashboardComponent)
  },
  {
    path: 'empresa/vagas/nova',
    canActivate: [authGuard, tipoUsuarioGuard('EMPRESA')],
    loadComponent: () =>
      import('./features/cadastro-vaga/cadastro-vaga.component').then(m => m.CadastroVagaComponent)
  }
];
