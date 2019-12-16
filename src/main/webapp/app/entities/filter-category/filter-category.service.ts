import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { FilterCategory } from './filter-category.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<FilterCategory>;

@Injectable()
export class FilterCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/filter-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/filter-categories';

    constructor(private http: HttpClient) {}

    create(filterCategory: FilterCategory): Observable<EntityResponseType> {
        const copy = this.convert(filterCategory);
        return this.http
            .post<FilterCategory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(filterCategory: FilterCategory): Observable<EntityResponseType> {
        const copy = this.convert(filterCategory);
        return this.http
            .put<FilterCategory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<FilterCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<FilterCategory[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<FilterCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<FilterCategory[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<FilterCategory[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<FilterCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<FilterCategory[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: FilterCategory = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<FilterCategory[]>): HttpResponse<FilterCategory[]> {
        const jsonResponse: FilterCategory[] = res.body;
        const body: FilterCategory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to FilterCategory.
     */
    private convertItemFromServer(filterCategory: FilterCategory): FilterCategory {
        const copy: FilterCategory = Object.assign({}, filterCategory);
        return copy;
    }

    /**
     * Convert a FilterCategory to a JSON which can be sent to the server.
     */
    private convert(filterCategory: FilterCategory): FilterCategory {
        const copy: FilterCategory = Object.assign({}, filterCategory);
        return copy;
    }
}
