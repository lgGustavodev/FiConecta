import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';


@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  selector: 'app-login-component',
  styleUrl: './login-component.scss',
  templateUrl: './login-component.html',
})

export class LoginComponent {
  erro = signal<string | null>(null);
  carregando = signal(false);

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.erro.set(null);
    this.carregando.set(true);

    this.authService.login(this.form.value).subscribe({
      next: (resposta) => {
        console.log('Login OK:', resposta);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Email ou senha inválidos.');
        this.carregando.set(false);
      }
    });
  }
}
