import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { FilterCategory } from './filter-category.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FilterCategoryService {

    private resourceUrl = SERVER_API_URL + 'api/filter-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/filter-categories';

    constructor(private http: Http) { }

    create(filterCategory: FilterCategory): Observable<FilterCategory> {
        const copy = this.convert(filterCategory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(filterCategory: FilterCategory): Observable<FilterCategory> {
        const copy = this.convert(filterCategory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<FilterCategory> {
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
     * Convert a returned JSON object to FilterCategory.
     */
    private convertItemFromServer(json: any): FilterCategory {
        const entity: FilterCategory = Object.assign(new FilterCategory(), json);
        return entity;
    }

    /**
     * Convert a FilterCategory to a JSON which can be sent to the server.
     */
    private convert(filterCategory: FilterCategory): FilterCategory {
        const copy: FilterCategory = Object.assign({}, filterCategory);
        return copy;
    }
}
