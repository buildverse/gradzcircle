import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { College } from './college.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<College>;

@Injectable()
export class CollegeService {
    private resourceUrl = SERVER_API_URL + 'api/colleges';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/colleges';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/collegesBySuggest';
    constructor(private http: HttpClient) {}

    create(college: College): Observable<EntityResponseType> {
        const copy = this.convert(college);
        return this.http
            .post<College>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(college: College): Observable<EntityResponseType> {
        const copy = this.convert(college);
        return this.http
            .put<College>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<College>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<College[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<College[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<College[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<College[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<College[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<College[]>) => this.convertArrayResponse(res));
    }

    /* searchRemote(req?: any): Observable<HttpResponse<College[]>> {
        const options = createRequestOption(req);
        return this.http.get<College[]>(this.resourceSearchSuggestUrl, { params: options, observe: 'response' })
                .map((res: HttpResponse<College[]>) => this.convertArrayResponse(res));
    }
*/
    searchRemote(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, { params: options, observe: 'response' });
    }
    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: College = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<College[]>): HttpResponse<College[]> {
        const jsonResponse: College[] = res.body;
        const body: College[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to College.
     */
    private convertItemFromServer(college: College): College {
        const copy: College = Object.assign({}, college);
        return copy;
    }

    /**
     * Convert a College to a JSON which can be sent to the server.
     */
    private convert(college: College): College {
        const copy: College = Object.assign({}, college);
        return copy;
    }
}
