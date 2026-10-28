import { Component, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { VagaService } from '../../core/services/vaga.service';

@Component({
  selector: 'app-cadastro-vaga',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './cadastro-vaga.component.html',
  styleUrl: './cadastro-vaga.component.scss'
})
export class CadastroVagaComponent {
  carregando = signal(false);
  erro = signal<string | null>(null);
  sucesso = signal(false);
  readonly hoje = new Date().toISOString().slice(0, 10);

  form;

  constructor(private fb: FormBuilder, private vagaService: VagaService, private router: Router) {
    this.form = this.fb.group({
    titulo: ['', [Validators.required, Validators.maxLength(150)]],
    descricao: [''],
    valor: [null as number | null, [Validators.required, Validators.min(0.01)]],
    dataVaga: ['', Validators.required],
    horarioInicio: ['', Validators.required],
    horarioFim: ['', Validators.required],
    endereco: this.fb.group({
      cep: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      logradouro: ['', [Validators.required, Validators.maxLength(150)]],
      numero: ['', [Validators.required, Validators.maxLength(10)]],
      complemento: ['', Validators.maxLength(100)],
      bairro: ['', [Validators.required, Validators.maxLength(100)]],
      cidade: ['', [Validators.required, Validators.maxLength(100)]],
      uf: ['', [Validators.required, Validators.pattern(/^[A-Z]{2}$/)]]
    })
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const dados = this.form.getRawValue();
    if (dados.horarioFim! <= dados.horarioInicio!) {
      this.erro.set('O horário de término deve ser posterior ao horário de início.');
      return;
    }

    this.erro.set(null);
    this.carregando.set(true);
    this.vagaService.cadastrar({
      titulo: dados.titulo!,
      descricao: dados.descricao || '',
      valor: dados.valor!,
      dataVaga: dados.dataVaga!,
      horarioInicio: dados.horarioInicio!,
      horarioFim: dados.horarioFim!,
      endereco: {
        cep: dados.endereco.cep!,
        logradouro: dados.endereco.logradouro!,
        numero: dados.endereco.numero!,
        complemento: dados.endereco.complemento || '',
        bairro: dados.endereco.bairro!,
        cidade: dados.endereco.cidade!,
        uf: dados.endereco.uf!
      }
    }).subscribe({
      next: () => {
        this.carregando.set(false);
        this.sucesso.set(true);
        setTimeout(() => this.router.navigateByUrl('/empresa/dashboard'), 1200);
      },
      error: () => {
        this.carregando.set(false);
        this.erro.set('Não foi possível cadastrar a vaga. Revise os dados e tente novamente.');
      }
    });
  }
}
