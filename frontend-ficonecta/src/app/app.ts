import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AcessibilidadeComponent } from './shared/components/acessibilidade/acessibilidade.component';

@Component({
  imports: [RouterOutlet, AcessibilidadeComponent],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend-ficonecta');
}
