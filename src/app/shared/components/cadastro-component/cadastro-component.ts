import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TipoUsuario } from '../../../core/models/auth.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  standalone: true,
  selector: 'app-cadastro-component',
  styleUrl: './cadastro-component.scss',
  templateUrl: './cadastro-component.html',
})
export class CadastroComponent {
  tipoSelecionado = signal<TipoUsuario>('PRESTADOR');
  erro = signal<string | null>(null);
  sucesso = signal(false);
  carregando = signal(false);

  isPrestador = computed(() => this.tipoSelecionado() === 'PRESTADOR');
  isEmpresa = computed(() => this.tipoSelecionado() === 'EMPRESA');

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      nome: ['', Validators.required],
      cpf: ['', Validators.required],
      tipoDocumento: ['CPF'],
      documento: ['']
    });

    this.selecionarTipo('PRESTADOR');
  }

  selecionarTipo(tipo: TipoUsuario): void {
    this.tipoSelecionado.set(tipo);
    this.erro.set(null);

    this.form.reset({ tipoDocumento: 'CPF' });

    if (tipo === 'PRESTADOR') {
      this.form.get('cpf')?.setValidators([Validators.required]);
      this.form.get('tipoDocumento')?.clearValidators();
      this.form.get('documento')?.clearValidators();
    } else {
      this.form.get('cpf')?.clearValidators();
      this.form.get('tipoDocumento')?.setValidators([Validators.required]);
      this.form.get('documento')?.setValidators([Validators.required]);
    }

    this.form.get('cpf')?.updateValueAndValidity();
    this.form.get('tipoDocumento')?.updateValueAndValidity();
    this.form.get('documento')?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.erro.set(null);
    this.carregando.set(true);
    const valores = this.form.value;

    const requisicao =
      this.tipoSelecionado() === 'PRESTADOR'
        ? this.authService.cadastrarPrestador({
            email: valores.email,
            senha: valores.senha,
            nome: valores.nome,
            cpf: valores.cpf
          })
        : this.authService.cadastrarEmpresa({
            email: valores.email,
            senha: valores.senha,
            nome: valores.nome,
            tipoDocumento: valores.tipoDocumento,
            documento: valores.documento
          });

    requisicao.subscribe({
      next: () => {
        this.carregando.set(false);
        this.sucesso.set(true);
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: () => {
        this.carregando.set(false);
        this.erro.set('Não foi possível cadastrar. Verifique os dados e tente novamente.');
      }
    });
  }
}