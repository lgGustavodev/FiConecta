import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PrestadorDashboard } from '../../core/models/dashboard.model';
import { DashboardService } from '../../core/services/dashboard.service';

@Component({
  selector: 'app-prestador-dashboard',
  imports: [RouterLink],
  templateUrl: './prestador-dashboard.component.html',
  styleUrl: './prestador-dashboard.component.scss'
})
export class PrestadorDashboardComponent implements OnInit {
  dashboard = signal<PrestadorDashboard | null>(null);
  erro = signal<string | null>(null);
  carregando = signal(true);

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.buscarDashboardPrestador().subscribe({
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
