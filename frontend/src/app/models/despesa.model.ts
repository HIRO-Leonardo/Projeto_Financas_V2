export type CategoriaEnum = 'ALIMENTACAO' | 'TRANSPORTE' | 'LAZER' | 'SALARIO' | 'SAUDE' | 'MORADIA' | 'DESPESA_ANIMAIS';

export const CATEGORIA_LABELS: Record<CategoriaEnum, string> = {
  ALIMENTACAO: 'Alimentação',
  TRANSPORTE: 'Transporte',
  LAZER: 'Lazer',
  SALARIO: 'Salário',
  SAUDE: 'Saúde',
  MORADIA: 'Moradia',
  DESPESA_ANIMAIS: 'Animais'
};

export const CATEGORIAS: CategoriaEnum[] = ['ALIMENTACAO', 'TRANSPORTE', 'LAZER', 'SALARIO', 'SAUDE', 'MORADIA', 'DESPESA_ANIMAIS'];

export interface Despesa {
  id: number;
  name: string;
  descricao: string;
  valor: number;
  localDateTime: string;
  categoriaEnum: CategoriaEnum;
  situacaoEnum: string;
  autor?: string;
}

export interface DespesaDTO {
  name: string;
  descricao: string;
  valor: number;
  localDateTime: string;
  categoriaEnum: CategoriaEnum;
  situacaoEnum: string;
  autor?: string;
}
