import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { Industry } from './industry.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Industry>;

@Injectable()
export class IndustryService {
    private resourceUrl = SERVER_API_URL + 'api/industries';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/industries';

    constructor(private http: HttpClient) {}

    create(industry: Industry): Observable<EntityResponseType> {
        const copy = this.convert(industry);
        return this.http
            .post<Industry>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(industry: Industry): Observable<EntityResponseType> {
        const copy = this.convert(industry);
        return this.http
            .put<Industry>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Industry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<Industry[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Industry[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Industry[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<Industry[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Industry[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Industry[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Industry = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<Industry[]>): HttpResponse<Industry[]> {
        const jsonResponse: Industry[] = res.body;
        const body: Industry[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to Industry.
     */
    private convertItemFromServer(industry: Industry): Industry {
        const copy: Industry = Object.assign({}, industry);
        return copy;
    }

    /**
     * Convert a Industry to a JSON which can be sent to the server.
     */
    private convert(industry: Industry): Industry {
        const copy: Industry = Object.assign({}, industry);
        return copy;
    }
}
