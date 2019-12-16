import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { JobFilterHistory } from './job-filter-history.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<JobFilterHistory>;

@Injectable()
export class JobFilterHistoryService {
    private resourceUrl = SERVER_API_URL + 'api/job-filter-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-filter-histories';

    constructor(private http: HttpClient) {}

    create(jobFilterHistory: JobFilterHistory): Observable<EntityResponseType> {
        const copy = this.convert(jobFilterHistory);
        return this.http
            .post<JobFilterHistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(jobFilterHistory: JobFilterHistory): Observable<EntityResponseType> {
        const copy = this.convert(jobFilterHistory);
        return this.http
            .put<JobFilterHistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<JobFilterHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<JobFilterHistory[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobFilterHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<JobFilterHistory[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<JobFilterHistory[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobFilterHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<JobFilterHistory[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: JobFilterHistory = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<JobFilterHistory[]>): HttpResponse<JobFilterHistory[]> {
        const jsonResponse: JobFilterHistory[] = res.body;
        const body: JobFilterHistory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to JobFilterHistory.
     */
    private convertItemFromServer(jobFilterHistory: JobFilterHistory): JobFilterHistory {
        const copy: JobFilterHistory = Object.assign({}, jobFilterHistory);
        return copy;
    }

    /**
     * Convert a JobFilterHistory to a JSON which can be sent to the server.
     */
    private convert(jobFilterHistory: JobFilterHistory): JobFilterHistory {
        const copy: JobFilterHistory = Object.assign({}, jobFilterHistory);
        return copy;
    }
}
