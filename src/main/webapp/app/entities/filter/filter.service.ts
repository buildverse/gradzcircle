import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Filter } from './filter.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FilterService {

    private resourceUrl = SERVER_API_URL + 'api/filters';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/filters';

    constructor(private http: Http) { }

    create(filter: Filter): Observable<Filter> {
        const copy = this.convert(filter);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(filter: Filter): Observable<Filter> {
        const copy = this.convert(filter);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Filter> {
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
     * Convert a returned JSON object to Filter.
     */
    private convertItemFromServer(json: any): Filter {
        const entity: Filter = Object.assign(new Filter(), json);
        return entity;
    }

    /**
     * Convert a Filter to a JSON which can be sent to the server.
     */
    private convert(filter: Filter): Filter {
        const copy: Filter = Object.assign({}, filter);
        return copy;
    }
}
