export interface Perfil {
  nome: string;
  email: string;
  descricao: string | null;
}

export interface PrestadorDashboard {
  perfil: Perfil;
  vagasAbertas: number;
}

export interface ResumoVagas {
  abertas: number;
  fechadas: number;
  canceladas: number;
  total: number;
}

export interface EmpresaDashboard {
  perfil: Perfil;
  resumoVagas: ResumoVagas;
}
