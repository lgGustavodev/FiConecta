import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

const apiUrl = 'http://localhost:8080/api';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // O token só deve acompanhar chamadas para a API do FI Conecta, nunca URLs externas.
  if (!request.url.startsWith(apiUrl) || !token) {
    return next(request);
  }

  return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
