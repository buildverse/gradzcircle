import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Skills } from './skills.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Skills>;

@Injectable()
export class SkillsService {

    private resourceUrl =  SERVER_API_URL + 'api/skills';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/skills';
     private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/skillBySuggest';

    constructor(private http: HttpClient) { }

    create(skills: Skills): Observable<EntityResponseType> {
        const copy = this.convert(skills);
        return this.http.post<Skills>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(skills: Skills): Observable<EntityResponseType> {
        const copy = this.convert(skills);
        return this.http.put<Skills>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    searchRemote(req?: any): Observable<HttpResponse<any>> {
      const options = createRequestOption(req);
      return this.http.get(this.resourceSearchSuggestUrl, {params: options, observe: 'response'});
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Skills>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Skills[]>> {
        const options = createRequestOption(req);
        return this.http.get<Skills[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Skills[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Skills[]>> {
        const options = createRequestOption(req);
        return this.http.get<Skills[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Skills[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Skills = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Skills[]>): HttpResponse<Skills[]> {
        const jsonResponse: Skills[] = res.body;
        const body: Skills[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Skills.
     */
    private convertItemFromServer(skills: Skills): Skills {
        const copy: Skills = Object.assign({}, skills);
        return copy;
    }

    /**
     * Convert a Skills to a JSON which can be sent to the server.
     */
    private convert(skills: Skills): Skills {
        const copy: Skills = Object.assign({}, skills);
        return copy;
    }
}
