import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EmpresaDashboard } from '../../core/models/dashboard.model';
import { DashboardService } from '../../core/services/dashboard.service';

@Component({
  selector: 'app-empresa-dashboard',
  imports: [RouterLink],
  templateUrl: './empresa-dashboard.component.html',
  styleUrl: './empresa-dashboard.component.scss'
})
export class EmpresaDashboardComponent implements OnInit {
  dashboard = signal<EmpresaDashboard | null>(null);
  erro = signal<string | null>(null);
  carregando = signal(true);

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.buscarDashboardEmpresa().subscribe({
      next: dashboard => {
        this.dashboard.set(dashboard);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar seu painel.');
        this.carregando.set(false);
      }
    });
  }
}
