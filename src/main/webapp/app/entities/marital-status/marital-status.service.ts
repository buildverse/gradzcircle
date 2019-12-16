import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { MaritalStatus } from './marital-status.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MaritalStatus>;

@Injectable()
export class MaritalStatusService {
    private resourceUrl = SERVER_API_URL + 'api/marital-statuses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/marital-statuses';

    constructor(private http: HttpClient) {}

    create(maritalStatus: MaritalStatus): Observable<EntityResponseType> {
        const copy = this.convert(maritalStatus);
        return this.http
            .post<MaritalStatus>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(maritalStatus: MaritalStatus): Observable<EntityResponseType> {
        const copy = this.convert(maritalStatus);
        return this.http
            .put<MaritalStatus>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<MaritalStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<MaritalStatus[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<MaritalStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<MaritalStatus[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<MaritalStatus[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<MaritalStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<MaritalStatus[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MaritalStatus = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<MaritalStatus[]>): HttpResponse<MaritalStatus[]> {
        const jsonResponse: MaritalStatus[] = res.body;
        const body: MaritalStatus[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to MaritalStatus.
     */
    private convertItemFromServer(maritalStatus: MaritalStatus): MaritalStatus {
        const copy: MaritalStatus = Object.assign({}, maritalStatus);
        return copy;
    }

    /**
     * Convert a MaritalStatus to a JSON which can be sent to the server.
     */
    private convert(maritalStatus: MaritalStatus): MaritalStatus {
        const copy: MaritalStatus = Object.assign({}, maritalStatus);
        return copy;
    }
}
