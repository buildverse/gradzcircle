import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { University } from './university.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<University>;

@Injectable()
export class UniversityService {
    private resourceUrl = SERVER_API_URL + 'api/universities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/universities';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/universitiesBySuggest';

    constructor(private http: HttpClient) {}

    create(university: University): Observable<EntityResponseType> {
        const copy = this.convert(university);
        return this.http
            .post<University>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(university: University): Observable<EntityResponseType> {
        const copy = this.convert(university);
        return this.http
            .put<University>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<University>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<University[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<University[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<University[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<University[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<University[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<University[]>) => this.convertArrayResponse(res));
    }

    searchRemote(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, { params: options, observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: University = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<University[]>): HttpResponse<University[]> {
        const jsonResponse: University[] = res.body;
        const body: University[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to University.
     */
    private convertItemFromServer(university: University): University {
        const copy: University = Object.assign({}, university);
        return copy;
    }

    /**
     * Convert a University to a JSON which can be sent to the server.
     */
    private convert(university: University): University {
        const copy: University = Object.assign({}, university);
        return copy;
    }
}
