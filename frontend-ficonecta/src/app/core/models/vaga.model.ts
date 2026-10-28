export interface CadastroVagaRequest {
  titulo: string;
  descricao: string;
  valor: number;
  dataVaga: string;
  horarioInicio: string;
  horarioFim: string;
  endereco: {
    cep: string;
    logradouro: string;
    numero: string;
    complemento: string;
    bairro: string;
    cidade: string;
    uf: string;
  };
}
