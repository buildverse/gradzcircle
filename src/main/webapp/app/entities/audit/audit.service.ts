import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Audit } from './audit.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AuditService {

    private resourceUrl = SERVER_API_URL + 'api/audits';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/audits';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(audit: Audit): Observable<Audit> {
        const copy = this.convert(audit);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(audit: Audit): Observable<Audit> {
        const copy = this.convert(audit);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Audit> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Audit.
     */
    private convertItemFromServer(json: any): Audit {
        const entity: Audit = Object.assign(new Audit(), json);
        entity.createdTime = this.dateUtils
            .convertDateTimeFromServer(json.createdTime);
        entity.updatedTime = this.dateUtils
            .convertDateTimeFromServer(json.updatedTime);
        return entity;
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
