export type TipoUsuario = 'PRESTADOR' | 'EMPRESA';

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  tipo: TipoUsuario;
}

export interface CadastroPrestadorRequest {
  email: string;
  senha: string;
  nome: string;
  cpf: string;
}

export interface CadastroEmpresaRequest {
  email: string;
  senha: string;
  nome: string;
  tipoDocumento: 'CPF' | 'CNPJ';
  documento: string;
}