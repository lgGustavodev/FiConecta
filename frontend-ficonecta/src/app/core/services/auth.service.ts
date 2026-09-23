import { Injectable } from '@angular/core';

import {
  LoginRequest,
  LoginResponse,
  CadastroPrestadorRequest,
  CadastroEmpresaRequest
} from '../models/auth.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  login(dados: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, dados);
  }

  cadastrarPrestador(dados: CadastroPrestadorRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/prestadores/cadastro`, dados);
  }

  cadastrarEmpresa(dados: CadastroEmpresaRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/empresas/cadastro`, dados);
  }
}
