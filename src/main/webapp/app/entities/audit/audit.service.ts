import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Audit } from './audit.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Audit>;

@Injectable()
export class AuditService {

    private resourceUrl =  SERVER_API_URL + 'api/audits';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/audits';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(audit: Audit): Observable<EntityResponseType> {
        const copy = this.convert(audit);
        return this.http.post<Audit>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(audit: Audit): Observable<EntityResponseType> {
        const copy = this.convert(audit);
        return this.http.put<Audit>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Audit>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Audit[]>> {
        const options = createRequestOption(req);
        return this.http.get<Audit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Audit[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Audit[]>> {
        const options = createRequestOption(req);
        return this.http.get<Audit[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Audit[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Audit = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Audit[]>): HttpResponse<Audit[]> {
        const jsonResponse: Audit[] = res.body;
        const body: Audit[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Audit.
     */
    private convertItemFromServer(audit: Audit): Audit {
        const copy: Audit = Object.assign({}, audit);
        copy.createdTime = this.dateUtils
            .convertDateTimeFromServer(audit.createdTime);
        copy.updatedTime = this.dateUtils
            .convertDateTimeFromServer(audit.updatedTime);
        return copy;
    }

    /**
     * Convert a Audit to a JSON which can be sent to the server.
     */
    private convert(audit: Audit): Audit {
        const copy: Audit = Object.assign({}, audit);

        copy.createdTime = this.dateUtils.toDate(audit.createdTime);

        copy.updatedTime = this.dateUtils.toDate(audit.updatedTime);
        return copy;
    }
}
