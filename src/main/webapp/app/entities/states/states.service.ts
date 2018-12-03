import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { States } from './states.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<States>;

@Injectable()
export class StatesService {

    private resourceUrl =  SERVER_API_URL + 'api/states';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/states';

    constructor(private http: HttpClient) { }

    create(states: States): Observable<EntityResponseType> {
        const copy = this.convert(states);
        return this.http.post<States>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(states: States): Observable<EntityResponseType> {
        const copy = this.convert(states);
        return this.http.put<States>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<States>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<States[]>> {
        const options = createRequestOption(req);
        return this.http.get<States[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<States[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<States[]>> {
        const options = createRequestOption(req);
        return this.http.get<States[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<States[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: States = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<States[]>): HttpResponse<States[]> {
        const jsonResponse: States[] = res.body;
        const body: States[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to States.
     */
    private convertItemFromServer(states: States): States {
        const copy: States = Object.assign({}, states);
        return copy;
    }

    /**
     * Convert a States to a JSON which can be sent to the server.
     */
    private convert(states: States): States {
        const copy: States = Object.assign({}, states);
        return copy;
    }
}
