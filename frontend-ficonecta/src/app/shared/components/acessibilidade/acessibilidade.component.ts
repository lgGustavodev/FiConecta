import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AcessibilidadeService } from '../../../core/services/acessibilidade.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-acessibilidade',
  templateUrl: './acessibilidade.component.html',
  styleUrl: './acessibilidade.component.scss'
})
export class AcessibilidadeComponent {
  protected readonly acessibilidade = inject(AcessibilidadeService);
  protected readonly authService = inject(AuthService);
  protected readonly menuAberto = signal(false);
  private readonly router = inject(Router);

  protected alternarMenu(): void {
    this.menuAberto.update(aberto => !aberto);
  }

  protected sair(): void {
    this.authService.encerrarSessao();
    this.menuAberto.set(false);
    this.router.navigateByUrl('/login');
  }
}
