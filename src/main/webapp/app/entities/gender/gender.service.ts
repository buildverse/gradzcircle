import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Gender } from './gender.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Gender>;

@Injectable()
export class GenderService {

    private resourceUrl =  SERVER_API_URL + 'api/genders';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/genders';

    constructor(private http: HttpClient) { }

    create(gender: Gender): Observable<EntityResponseType> {
        const copy = this.convert(gender);
        return this.http.post<Gender>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(gender: Gender): Observable<EntityResponseType> {
        const copy = this.convert(gender);
        return this.http.put<Gender>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Gender>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Gender[]>> {
        const options = createRequestOption(req);
        return this.http.get<Gender[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Gender[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Gender[]>> {
        const options = createRequestOption(req);
        return this.http.get<Gender[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Gender[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Gender = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Gender[]>): HttpResponse<Gender[]> {
        const jsonResponse: Gender[] = res.body;
        const body: Gender[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Gender.
     */
    private convertItemFromServer(gender: Gender): Gender {
        const copy: Gender = Object.assign({}, gender);
        return copy;
    }

    /**
     * Convert a Gender to a JSON which can be sent to the server.
     */
    private convert(gender: Gender): Gender {
        const copy: Gender = Object.assign({}, gender);
        return copy;
    }
}
