export type CategoriaReceitaEnum = 'SALARIO' | 'FREELANCE' | 'INVESTIMENTOS' | 'RENDA_EXTRA' | 'PRESENTE' | 'REEMBOLSO' | 'VENDA' | 'OUTROS';

export const CATEGORIA_RECEITA_LABELS: Record<CategoriaReceitaEnum, string> = {
  SALARIO: 'Salário',
  FREELANCE: 'Freelance',
  INVESTIMENTOS: 'Investimentos',
  RENDA_EXTRA: 'Renda Extra',
  PRESENTE: 'Presente',
  REEMBOLSO: 'Reembolso',
  VENDA: 'Venda',
  OUTROS: 'Outros'
};

export const CATEGORIAS_RECEITA: CategoriaReceitaEnum[] = ['SALARIO', 'FREELANCE', 'INVESTIMENTOS', 'RENDA_EXTRA', 'PRESENTE', 'REEMBOLSO', 'VENDA', 'OUTROS'];

export interface Receita {
  id: number;
  name: string;
  descricao: string;
  valor: number;
  categoriaReceitaEnum: CategoriaReceitaEnum;
  localDateTimeEntrada: string;
  situacaoEnum: string;
  autor?: string;
}

export interface ReceitaDTO {
  name: string;
  valor: number;
  descricao: string;
  categoriaReceitaEnum: CategoriaReceitaEnum;
  localDateTimeEntrada: string;
  situacaoEnum: string;
  autor?: string;
}
