import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Despesa, DespesaDTO } from '../models/despesa.model';
import { TotalPorCategoria } from '../models/shared.model';

@Injectable({ providedIn: 'root' })
export class DespesaService {
  private http = inject(HttpClient);
  private baseUrl = '/despesa';

  listAll(): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(this.baseUrl);
  }

  listPaginated(page: number, size: number): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(`${this.baseUrl}/paginacao?page=${page}&size=${size}`);
  }

  create(dto: DespesaDTO): Observable<any> {
    return this.http.post(this.baseUrl, dto);
  }

  update(id: number, dto: DespesaDTO): Observable<Despesa> {
    return this.http.put<Despesa>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getSaldoBySituacao(situacao: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/saldo/${situacao}`);
  }

  buscarPorMesAno(mes: number, ano: number): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(`${this.baseUrl}/buscar/${mes}-${ano}`);
  }

  relatorioMensalCategoria(mes: number, ano: number): Observable<TotalPorCategoria[]> {
    return this.http.get<TotalPorCategoria[]>(`${this.baseUrl}/relatorio-mensal-categoria/${mes}-${ano}`);
  }

  compararDoisMeses(ano1: number, mes1: number, ano2: number, mes2: number): Observable<Despesa[][]> {
    return this.http.get<Despesa[][]>(`${this.baseUrl}/comparationbetweentwomonth/${ano1}-${mes1}-${ano2}-${mes2}`);
  }
}
