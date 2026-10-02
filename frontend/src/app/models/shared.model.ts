import { Despesa } from './despesa.model';
import { Receita } from './receita.model';

export type SituacaoEnum = 'A_PAGAR' | 'PAGA' | 'RECEBIDO' | 'A_RECEBER';

export const SITUACAO_LABELS: Record<SituacaoEnum, string> = {
  A_PAGAR: 'A Pagar',
  PAGA: 'Paga',
  RECEBIDO: 'Recebido',
  A_RECEBER: 'A Receber'
};

export const SITUACOES: SituacaoEnum[] = ['A_PAGAR', 'PAGA', 'RECEBIDO', 'A_RECEBER'];

export interface TotalPorCategoria {
  categoria: string;
  valor: number;
}

export interface Contabilidade {
  receita: Receita[];
  despesa: Despesa[];
}

export const MESES: { value: number; label: string }[] = [
  { value: 1, label: 'Janeiro' },
  { value: 2, label: 'Fevereiro' },
  { value: 3, label: 'Março' },
  { value: 4, label: 'Abril' },
  { value: 5, label: 'Maio' },
  { value: 6, label: 'Junho' },
  { value: 7, label: 'Julho' },
  { value: 8, label: 'Agosto' },
  { value: 9, label: 'Setembro' },
  { value: 10, label: 'Outubro' },
  { value: 11, label: 'Novembro' },
  { value: 12, label: 'Dezembro' }
];
