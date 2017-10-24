import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Employability } from './employability.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmployabilityService {

    private resourceUrl = SERVER_API_URL + 'api/employabilities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employabilities';

    constructor(private http: Http) { }

    create(employability: Employability): Observable<Employability> {
        const copy = this.convert(employability);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(employability: Employability): Observable<Employability> {
        const copy = this.convert(employability);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Employability> {
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
     * Convert a returned JSON object to Employability.
     */
    private convertItemFromServer(json: any): Employability {
        const entity: Employability = Object.assign(new Employability(), json);
        return entity;
    }

    /**
     * Convert a Employability to a JSON which can be sent to the server.
     */
    private convert(employability: Employability): Employability {
        const copy: Employability = Object.assign({}, employability);
        return copy;
    }
}
