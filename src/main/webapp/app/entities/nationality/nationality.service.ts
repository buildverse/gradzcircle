import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Nationality } from './nationality.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Nationality>;

@Injectable()
export class NationalityService {

    private resourceUrl =  SERVER_API_URL + 'api/nationalities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/nationalities';

    constructor(private http: HttpClient) { }

    create(nationality: Nationality): Observable<EntityResponseType> {
        const copy = this.convert(nationality);
        return this.http.post<Nationality>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(nationality: Nationality): Observable<EntityResponseType> {
        const copy = this.convert(nationality);
        return this.http.put<Nationality>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Nationality>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Nationality[]>> {
        const options = createRequestOption(req);
        return this.http.get<Nationality[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Nationality[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Nationality[]>> {
        const options = createRequestOption(req);
        return this.http.get<Nationality[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Nationality[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Nationality = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Nationality[]>): HttpResponse<Nationality[]> {
        const jsonResponse: Nationality[] = res.body;
        const body: Nationality[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Nationality.
     */
    private convertItemFromServer(nationality: Nationality): Nationality {
        const copy: Nationality = Object.assign({}, nationality);
        return copy;
    }

    /**
     * Convert a Nationality to a JSON which can be sent to the server.
     */
    private convert(nationality: Nationality): Nationality {
        const copy: Nationality = Object.assign({}, nationality);
        return copy;
    }
}
