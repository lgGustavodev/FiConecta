import { DOCUMENT } from '@angular/common';
import { Injectable, inject, signal } from '@angular/core';

type Tema = 'claro' | 'escuro';

@Injectable({ providedIn: 'root' })
export class AcessibilidadeService {
  private readonly document = inject(DOCUMENT);
  private readonly temaKey = 'ficonecta_tema';
  private readonly fonteKey = 'ficonecta_escala_fonte';
  readonly escalaFonte = signal(this.lerEscala());
  readonly tema = signal<Tema>(this.lerTema());

  constructor() {
    this.aplicarPreferencias();
  }

  aumentarFonte(): void {
    this.definirEscala(Math.min(this.escalaFonte() + 0.1, 1.4));
  }

  diminuirFonte(): void {
    this.definirEscala(Math.max(this.escalaFonte() - 0.1, 0.9));
  }

  alternarTema(): void {
    this.tema.set(this.tema() === 'claro' ? 'escuro' : 'claro');
    localStorage.setItem(this.temaKey, this.tema());
    this.aplicarPreferencias();
  }

  private definirEscala(escala: number): void {
    const valor = Number(escala.toFixed(1));
    this.escalaFonte.set(valor);
    localStorage.setItem(this.fonteKey, String(valor));
    this.aplicarPreferencias();
  }

  private aplicarPreferencias(): void {
    const raiz = this.document.documentElement;
    raiz.style.fontSize = `${this.escalaFonte() * 100}%`;
    raiz.dataset['tema'] = this.tema();
  }

  private lerEscala(): number {
    const valor = Number(localStorage.getItem(this.fonteKey));
    return valor >= 0.9 && valor <= 1.4 ? valor : 1;
  }

  private lerTema(): Tema {
    return localStorage.getItem(this.temaKey) === 'escuro' ? 'escuro' : 'claro';
  }
}
