import {Injectable} from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';
import {CandidateLanguageProficiency} from './candidate-language-proficiency.model';
import { createRequestOption} from '../../shared';

export type EntityResponseType = HttpResponse<CandidateLanguageProficiency>;

@Injectable()
export class CandidateLanguageProficiencyService {

  private resourceUrl = SERVER_API_URL + 'api/candidate-language-proficiencies';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-language-proficiencies';
  private resourceUrlByCandidate = SERVER_API_URL + 'api/language-proficiencies-by-candidate';
  private resourceUrlMultiple = SERVER_API_URL + 'api/candidate-languages-proficiency';


 constructor(private http: HttpClient) { }

  create(candidateLanguageProficiency: CandidateLanguageProficiency): Observable<EntityResponseType> {
        const copy = this.convert(candidateLanguageProficiency);
        return this.http.post<CandidateLanguageProficiency>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

  createMultiple(candidateLanguageProficiency: CandidateLanguageProficiency): Observable<EntityResponseType> {
    const copy = this.convert(candidateLanguageProficiency);
    // console.log("Sending over wire ...create"+ JSON.stringify(candidateLanguageProficiency));
    return this.http.post<CandidateLanguageProficiency>(this.resourceUrlMultiple, copy, { observe: 'response' })
      .map((res: EntityResponseType) => this.convertResponse(res));
  }
  
   update(candidateLanguageProficiency: CandidateLanguageProficiency): Observable<EntityResponseType> {
        const copy = this.convert(candidateLanguageProficiency);
        return this.http.put<CandidateLanguageProficiency>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

   find(id: number): Observable<EntityResponseType> {
          return this.http.get<CandidateLanguageProficiency>(`${this.resourceUrl}/${id}`, { observe: 'response'})
              .map((res: EntityResponseType) => this.convertResponse(res));
      }

  findByCandidateId(id: number): Observable<HttpResponse<CandidateLanguageProficiency[]>> {
    console.log('CAALNG FOR CANDIDATE ONLY');
    return this.http.get<CandidateLanguageProficiency[]>(`${this.resourceUrlByCandidate}/${id}`, {  observe: 'response' })
            .map((res: HttpResponse<CandidateLanguageProficiency[]>) => this.convertArrayResponse(res));
  }
  
  query(req?: any): Observable<HttpResponse<CandidateLanguageProficiency[]>> {
        const options = createRequestOption(req);
        return this.http.get<CandidateLanguageProficiency[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CandidateLanguageProficiency[]>) => this.convertArrayResponse(res));
    }

  delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

  search(req?: any): Observable<HttpResponse<CandidateLanguageProficiency[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateLanguageProficiency[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CandidateLanguageProficiency[]>) => this.convertArrayResponse(res));
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CandidateLanguageProficiency = this.convertItemFromServer(res.body);
        return res.clone({body});
    }
  
   private convertArrayResponse(res: HttpResponse<CandidateLanguageProficiency[]>): HttpResponse<CandidateLanguageProficiency[]> {
        const jsonResponse: CandidateLanguageProficiency[] = res.body;
        const body: CandidateLanguageProficiency[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

  /**
   * Convert a returned JSON object to CandidateLanguageProficiency.
   */
 private convertItemFromServer(candidateLanguageProficiency: CandidateLanguageProficiency): CandidateLanguageProficiency {
        const copy: CandidateLanguageProficiency = Object.assign({}, candidateLanguageProficiency);
        return copy;
  }

  /**
   * Convert a CandidateLanguageProficiency to a JSON which can be sent to the server.
   */
   private convert(candidateLanguageProficiency: CandidateLanguageProficiency): CandidateLanguageProficiency {
        const copy: CandidateLanguageProficiency = Object.assign({}, candidateLanguageProficiency);
        return copy;
  }
}
