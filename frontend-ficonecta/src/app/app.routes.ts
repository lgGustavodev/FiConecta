import { Routes } from '@angular/router';

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
  }
];