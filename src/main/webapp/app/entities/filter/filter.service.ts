import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { Filter } from './filter.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Filter>;

@Injectable()
export class FilterService {
    private resourceUrl = SERVER_API_URL + 'api/filters';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/filters';

    constructor(private http: HttpClient) {}

    create(filter: Filter): Observable<EntityResponseType> {
        const copy = this.convert(filter);
        return this.http
            .post<Filter>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(filter: Filter): Observable<EntityResponseType> {
        const copy = this.convert(filter);
        return this.http
            .put<Filter>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Filter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<Filter[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Filter[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Filter[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<Filter[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Filter[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Filter[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Filter = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<Filter[]>): HttpResponse<Filter[]> {
        const jsonResponse: Filter[] = res.body;
        const body: Filter[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to Filter.
     */
    private convertItemFromServer(filter: Filter): Filter {
        const copy: Filter = Object.assign({}, filter);
        return copy;
    }

    /**
     * Convert a Filter to a JSON which can be sent to the server.
     */
    private convert(filter: Filter): Filter {
        const copy: Filter = Object.assign({}, filter);
        return copy;
    }
}
