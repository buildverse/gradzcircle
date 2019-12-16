import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { Employability } from './employability.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Employability>;

@Injectable()
export class EmployabilityService {
    private resourceUrl = SERVER_API_URL + 'api/employabilities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employabilities';

    constructor(private http: HttpClient) {}

    create(employability: Employability): Observable<EntityResponseType> {
        const copy = this.convert(employability);
        return this.http
            .post<Employability>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(employability: Employability): Observable<EntityResponseType> {
        const copy = this.convert(employability);
        return this.http
            .put<Employability>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Employability>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<Employability[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Employability[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Employability[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<Employability[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Employability[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Employability[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Employability = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<Employability[]>): HttpResponse<Employability[]> {
        const jsonResponse: Employability[] = res.body;
        const body: Employability[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to Employability.
     */
    private convertItemFromServer(employability: Employability): Employability {
        const copy: Employability = Object.assign({}, employability);
        return copy;
    }

    /**
     * Convert a Employability to a JSON which can be sent to the server.
     */
    private convert(employability: Employability): Employability {
        const copy: Employability = Object.assign({}, employability);
        return copy;
    }
}
