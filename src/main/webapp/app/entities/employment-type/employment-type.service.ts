import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { EmploymentType } from './employment-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<EmploymentType>;

@Injectable()
export class EmploymentTypeService {

    private resourceUrl =  SERVER_API_URL + 'api/employment-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employment-types';

    constructor(private http: HttpClient) { }

    create(employmentType: EmploymentType): Observable<EntityResponseType> {
        const copy = this.convert(employmentType);
        return this.http.post<EmploymentType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(employmentType: EmploymentType): Observable<EntityResponseType> {
        const copy = this.convert(employmentType);
        return this.http.put<EmploymentType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<EmploymentType>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<EmploymentType[]>> {
        const options = createRequestOption(req);
        return this.http.get<EmploymentType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<EmploymentType[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<EmploymentType[]>> {
        const options = createRequestOption(req);
        return this.http.get<EmploymentType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<EmploymentType[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: EmploymentType = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<EmploymentType[]>): HttpResponse<EmploymentType[]> {
        const jsonResponse: EmploymentType[] = res.body;
        const body: EmploymentType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to EmploymentType.
     */
    private convertItemFromServer(employmentType: EmploymentType): EmploymentType {
        const copy: EmploymentType = Object.assign({}, employmentType);
        return copy;
    }

    /**
     * Convert a EmploymentType to a JSON which can be sent to the server.
     */
    private convert(employmentType: EmploymentType): EmploymentType {
        const copy: EmploymentType = Object.assign({}, employmentType);
        return copy;
    }
}
