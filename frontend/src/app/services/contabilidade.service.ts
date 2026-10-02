import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contabilidade } from '../models/shared.model';

@Injectable({ providedIn: 'root' })
export class ContabilidadeService {
  private http = inject(HttpClient);
  private baseUrl = '/contabilidade';

  getContabilidade(ano: number, mes: number): Observable<Contabilidade> {
    return this.http.get<Contabilidade>(`${this.baseUrl}/${ano}-${mes}`);
  }
}
