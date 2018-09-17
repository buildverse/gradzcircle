import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL, ENABLE_ELASTIC} from '../../app.constants';
import {CandidateLanguageProficiency} from './candidate-language-proficiency.model';
import {ResponseWrapper, createRequestOption} from '../../shared';

@Injectable()
export class CandidateLanguageProficiencyService {

  private resourceUrl = SERVER_API_URL + 'api/candidate-language-proficiencies';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-language-proficiencies';
  private resourceUrlByCandidate = SERVER_API_URL + 'api/language-proficiencies-by-candidate';
  private resourceUrlMultiple = SERVER_API_URL + 'api/candidate-languages-proficiency';


  constructor(private http: Http) {}

  create(candidateLanguageProficiency: CandidateLanguageProficiency): Observable<CandidateLanguageProficiency> {
    const copy = this.convert(candidateLanguageProficiency);
    return this.http.post(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  createMultiple(candidateLanguageProficiency: CandidateLanguageProficiency): Observable<CandidateLanguageProficiency> {
    const copy = this.convert(candidateLanguageProficiency);
    // console.log("Sending over wire ...create"+ JSON.stringify(candidateLanguageProficiency));
    return this.http.post(this.resourceUrlMultiple, candidateLanguageProficiency).map((res: Response) => {
      return res.json();
    });
  }
  update(candidateLanguageProficiency: CandidateLanguageProficiency): Observable<CandidateLanguageProficiency> {
    const copy = this.convert(candidateLanguageProficiency);
    return this.http.put(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  find(id: number): Observable<CandidateLanguageProficiency> {
    return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  findAll(): Observable<ResponseWrapper> {
    return this.http.get(`${this.resourceUrl}`)
      .map((res: any) => this.convertResponse(res));
  }

  findByCandidateId(id: number): Observable<ResponseWrapper> {
    console.log('CAALNG FOR CANDIDATE ONLY');
    return this.http.get(`${this.resourceUrlByCandidate}/${id}`)
      .map((res: any) => this.convertResponse(res));
  }

  query(req?: any): Observable<ResponseWrapper> {
    if (!ENABLE_ELASTIC && !req) {
      console.log('calling first if');
      return this.findAll();
    } else {
      if (!ENABLE_ELASTIC && req) {
        console.log('calling second if');
        return this.findByCandidateId(req);
      } else {
        console.log('calling else');
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
          .map((res: Response) => this.convertResponse(res));
      }
    }

  }

  delete(id: number): Observable<Response> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  search(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, options)
      .map((res: any) => this.convertResponse(res));
  }

  private convertResponse(res: Response): ResponseWrapper {
    const jsonResponse = res.json();
    const result = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      result.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return new ResponseWrapper(res.headers, result, res.status);
  }

  /**
   * Convert a returned JSON object to CandidateLanguageProficiency.
   */
  private convertItemFromServer(json: any): CandidateLanguageProficiency {
    const entity: CandidateLanguageProficiency = Object.assign(new CandidateLanguageProficiency(), json);
    console.log('After create reutnirng ' + JSON.stringify(entity));
    return entity;
  }

  /**
   * Convert a CandidateLanguageProficiency to a JSON which can be sent to the server.
   */
  private convert(candidateLanguageProficiency: CandidateLanguageProficiency): CandidateLanguageProficiency {
    const copy: CandidateLanguageProficiency = Object.assign({}, candidateLanguageProficiency);
    return copy;
  }
}
