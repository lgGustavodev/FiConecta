import { Injectable, signal } from '@angular/core';

import {
  LoginRequest,
  LoginResponse,
  CadastroPrestadorRequest,
  CadastroEmpresaRequest,
  TipoUsuario
} from '../models/auth.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly baseUrl = 'http://localhost:8080/api';
  private readonly tokenKey = 'ficonecta_token';
  private readonly tipoKey = 'ficonecta_tipo_usuario';
  readonly sessaoAtiva = signal(this.estaAutenticado());

  constructor(private http: HttpClient) {}

  login(dados: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, dados);
  }

  salvarSessao(resposta: LoginResponse): void {
    localStorage.setItem(this.tokenKey, resposta.token);
    localStorage.setItem(this.tipoKey, resposta.tipo);
    this.sessaoAtiva.set(true);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getTipoUsuario(): TipoUsuario | null {
    const tipo = localStorage.getItem(this.tipoKey);
    return tipo === 'PRESTADOR' || tipo === 'EMPRESA' ? tipo : null;
  }

  estaAutenticado(): boolean {
    return !!this.getToken() && !!this.getTipoUsuario();
  }

  encerrarSessao(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.tipoKey);
    this.sessaoAtiva.set(false);
  }

  cadastrarPrestador(dados: CadastroPrestadorRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/prestadores/cadastro`, dados);
  }

  cadastrarEmpresa(dados: CadastroEmpresaRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/empresas/cadastro`, dados);
  }
}
