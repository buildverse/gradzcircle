import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';
import {CandidateList} from '../job/candidate-list.model';
import {Corporate} from './corporate.model';
import {createRequestOption} from '../../shared';
import {Country} from '../../entities/country/country.model';
export type EntityResponseType = HttpResponse<Corporate>;
export type EntityResponseTypeCandidateList = HttpResponse<CandidateList>;

@Injectable()
export class CorporateService {

  private resourceUrl = SERVER_API_URL + 'api/corporates';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/corporates';
  private findByLoginIdresourceUrl = SERVER_API_URL + 'api/corporateByLoginId';
  private resourceLinkedCandidatesForCorporateUrl = SERVER_API_URL + 'api/linkedCandidatesForCorporate';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(corporate: Corporate): Observable<EntityResponseType> {
    const copy = this.convert(corporate);
    return this.http.post<Corporate>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  update(corporate: Corporate): Observable<EntityResponseType> {
    const copy = this.convert(corporate);
    return this.http.put<Corporate>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<Corporate>(`${this.resourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  findCorporateByLoginId(id: number): Observable<EntityResponseType> {
    return this.http.get<Corporate>(`${this.findByLoginIdresourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  queryLinkedCandidates(req?: any): Observable<HttpResponse<CandidateList[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateList[]>(`${this.resourceLinkedCandidatesForCorporateUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateList[]>) => this.convertArrayResponseForCandidateList(res));
  }

  query(req?: any): Observable<HttpResponse<Corporate[]>> {
    const options = createRequestOption(req);
    return this.http.get<Corporate[]>(this.resourceUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<Corporate[]>) => this.convertArrayResponseForCorporate(res));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }



  search(req?: any): Observable<HttpResponse<Corporate[]>> {
    const options = createRequestOption(req);
    return this.http.get<Corporate[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<Corporate[]>) => this.convertArrayResponseForCorporate(res));
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
    const body: Corporate = this.convertItemFromServer(res.body);
    this.convertMetaDataForDisplay(body);
    return res.clone({body});
  }
  
  private convertMetaDataForDisplay(corporate: Corporate) {
    if (corporate.country && corporate.country) {
      const countryArray = new Array();
      countryArray.push(corporate.country);
      corporate.country = countryArray;
    }
  }
  
  convertMetaDataForServer(corporate: Corporate) {
    const country = new Country();
    country.countryNiceName = corporate.country[0].value;
    corporate.country = country;
  }

  /**
   * Convert a returned JSON object to Corporate.
   */
  private convertItemFromServer(corporate: Corporate): Corporate {
    const copy: Corporate = Object.assign({}, corporate);
    copy.establishedSince = this.dateUtils
      .convertLocalDateFromServer(corporate.establishedSince);
    return copy;
  }

  /**
   * Convert a Corporate to a JSON which can be sent to the server.
   */
  private convert(corporate: Corporate): Corporate {
    const copy: Corporate = Object.assign({}, corporate);
    copy.establishedSince = this.dateUtils
      .convertLocalDateToServer(corporate.establishedSince);
    this.convertMetaDataForServer(copy);
    return copy;
  }

  private convertArrayResponseForCorporate(res: HttpResponse<Corporate[]>): HttpResponse<Corporate[]> {
    const jsonResponse: Corporate[] = res.body;
    const body: Corporate[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  private convertArrayResponseForCandidateList(res: HttpResponse<CandidateList[]>): HttpResponse<CandidateList[]> {
    const jsonResponse: CandidateList[] = res.body;
    const body: CandidateList[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  
}
