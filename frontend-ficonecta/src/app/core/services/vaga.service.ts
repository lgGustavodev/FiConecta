import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CadastroVagaRequest } from '../models/vaga.model';

@Injectable({ providedIn: 'root' })
export class VagaService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  cadastrar(dados: CadastroVagaRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/empresas/vagas`, dados);
  }
}
