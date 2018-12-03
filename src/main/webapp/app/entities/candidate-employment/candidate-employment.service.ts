import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';
import {SERVER_API_URL} from '../../app.constants';
import {CandidateEmployment} from './candidate-employment.model';
import {createRequestOption} from '../../shared';

export type EntityResponseType = HttpResponse<CandidateEmployment>;

@Injectable()
export class CandidateEmploymentService {

  private resourceUrl = SERVER_API_URL + 'api/candidate-employments';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-employments';
  private resourceUrlByCandidate = SERVER_API_URL + 'api/employment-by-candidate'


  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}



  create(candidateEmployment: CandidateEmployment): Observable<EntityResponseType> {
    const copy = this.convert(candidateEmployment);
    return this.http.post<CandidateEmployment>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));

  }

  update(candidateEmployment: CandidateEmployment): Observable<EntityResponseType> {
    const copy = this.convert(candidateEmployment);
    return this.http.put<CandidateEmployment>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<CandidateEmployment>(`${this.resourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }


  findEmploymentsByCandidateId(id?: number): Observable<HttpResponse<CandidateEmployment[]>> {
    //const options = this.createRequestOption(req);
    return this.http.get(`${this.resourceUrlByCandidate}/${id}`, {observe: 'response'})
      .map((res: HttpResponse<CandidateEmployment[]>) => this.convertArrayResponse(res));
  }

  query(req?: any): Observable<HttpResponse<CandidateEmployment[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateEmployment[]>(this.resourceUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateEmployment[]>) => this.convertArrayResponse(res));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  search(req?: any): Observable<HttpResponse<CandidateEmployment[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateEmployment[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateEmployment[]>) => this.convertArrayResponse(res));
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
    const body: CandidateEmployment = this.convertItemFromServer(res.body);
    return res.clone({body});
  }

  /**
   * Convert a returned JSON object to CandidateEmployment.
   */
  private convertItemFromServer(candidateEmployment: CandidateEmployment): CandidateEmployment {
    const copy: CandidateEmployment = Object.assign({}, candidateEmployment);
    copy.employmentStartDate = this.dateUtils
      .convertLocalDateFromServer(candidateEmployment.employmentStartDate);
    copy.employmentEndDate = this.dateUtils
      .convertLocalDateFromServer(candidateEmployment.employmentEndDate);
    copy.collapsed = true;
    if (copy.projects) {
      copy.projects.forEach((project) => {
        project.collapsed = true;
      });
    }
    return copy;
  }

  private convertArrayResponse(res: HttpResponse<CandidateEmployment[]>): HttpResponse<CandidateEmployment[]> {
    const jsonResponse: CandidateEmployment[] = res.body;
    const body: CandidateEmployment[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  /**
   * Convert a CandidateEmployment to a JSON which can be sent to the server.
   */
  private convert(candidateEmployment: CandidateEmployment): CandidateEmployment {
    const copy: CandidateEmployment = Object.assign({}, candidateEmployment);
    copy.employmentStartDate = this.dateUtils
      .convertLocalDateToServer(candidateEmployment.employmentStartDate);
    copy.employmentEndDate = this.dateUtils
      .convertLocalDateToServer(candidateEmployment.employmentEndDate);
    return copy;
  }
}
