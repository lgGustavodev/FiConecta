import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TipoUsuario } from '../models/auth.model';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.estaAutenticado() || router.createUrlTree(['/login']);
};

export const tipoUsuarioGuard = (tipoPermitido: TipoUsuario): CanActivateFn => () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.getTipoUsuario() === tipoPermitido) return true;

  const destino = authService.getTipoUsuario() === 'EMPRESA'
    ? '/empresa/dashboard'
    : '/prestador/dashboard';
  return router.createUrlTree([destino]);
};
