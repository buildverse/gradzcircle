import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Qualification } from './qualification.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Qualification>;

@Injectable()
export class QualificationService {

    private resourceUrl =  SERVER_API_URL + 'api/qualifications';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/qualifications';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/qualificationsBySuggest';
  
    constructor(private http: HttpClient) { }

    create(qualification: Qualification): Observable<EntityResponseType> {
        const copy = this.convert(qualification);
        return this.http.post<Qualification>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(qualification: Qualification): Observable<EntityResponseType> {
        const copy = this.convert(qualification);
        return this.http.put<Qualification>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Qualification>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Qualification[]>> {
        const options = createRequestOption(req);
        return this.http.get<Qualification[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Qualification[]>) => this.convertArrayResponse(res));
    }
    
    searchRemote(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, { params: options, observe: 'response' });
  }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Qualification[]>> {
        const options = createRequestOption(req);
        return this.http.get<Qualification[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Qualification[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Qualification = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Qualification[]>): HttpResponse<Qualification[]> {
        const jsonResponse: Qualification[] = res.body;
        const body: Qualification[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Qualification.
     */
    private convertItemFromServer(qualification: Qualification): Qualification {
        const copy: Qualification = Object.assign({}, qualification);
        return copy;
    }

    /**
     * Convert a Qualification to a JSON which can be sent to the server.
     */
    private convert(qualification: Qualification): Qualification {
        const copy: Qualification = Object.assign({}, qualification);
        return copy;
    }
}
