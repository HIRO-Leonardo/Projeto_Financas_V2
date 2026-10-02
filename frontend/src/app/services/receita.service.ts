import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Receita, ReceitaDTO } from '../models/receita.model';

@Injectable({ providedIn: 'root' })
export class ReceitaService {
  private http = inject(HttpClient);
  private baseUrl = '/receita';

  listAll(filters?: { categoriaReceitaEnum?: string; situacaoEnum?: string; mes?: number }): Observable<Receita[]> {
    let params = new HttpParams();
    if (filters) {
      if (filters.categoriaReceitaEnum) params = params.set('categoriaReceitaEnum', filters.categoriaReceitaEnum);
      if (filters.situacaoEnum) params = params.set('situacaoEnum', filters.situacaoEnum);
      if (filters.mes) params = params.set('mes', filters.mes.toString());
    }
    return this.http.get<Receita[]>(this.baseUrl, { params });
  }

  listPaginated(page: number, size: number): Observable<Receita[]> {
    return this.http.get<Receita[]>(`${this.baseUrl}/paginacao?page=${page}&size=${size}`);
  }

  create(dto: ReceitaDTO): Observable<any> {
    return this.http.post(this.baseUrl, dto);
  }

  update(id: number, dto: ReceitaDTO): Observable<Receita> {
    return this.http.put<Receita>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getSaldoByCategoria(categoria: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/saldo/${categoria}`);
  }

  getSaldoBySituacao(situacao: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/saldo_situacao/${situacao}`);
  }

  buscarPorMesAno(mes: number, ano: number): Observable<Receita[]> {
    return this.http.get<Receita[]>(`${this.baseUrl}/buscar/${mes}-${ano}`);
  }

  compararDoisMeses(ano1: number, mes1: number, ano2: number, mes2: number): Observable<Receita[][]> {
    return this.http.get<Receita[][]>(`${this.baseUrl}/comparationbetweentwomonth/${ano1}-${mes1}-${ano2}-${mes2}`);
  }
}
