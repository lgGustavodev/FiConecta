import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmpresaDashboard, PrestadorDashboard } from '../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  buscarDashboardPrestador(): Observable<PrestadorDashboard> {
    return this.http.get<PrestadorDashboard>(`${this.baseUrl}/prestadores/dashboard`);
  }

  buscarDashboardEmpresa(): Observable<EmpresaDashboard> {
    return this.http.get<EmpresaDashboard>(`${this.baseUrl}/empresas/dashboard`);
  }
}
