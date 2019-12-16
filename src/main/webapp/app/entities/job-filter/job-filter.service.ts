import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { JobFilter } from './job-filter.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<JobFilter>;

@Injectable()
export class JobFilterService {
    private resourceUrl = SERVER_API_URL + 'api/job-filters';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-filters';

    constructor(private http: HttpClient) {}

    create(jobFilter: JobFilter): Observable<EntityResponseType> {
        const copy = this.convert(jobFilter);
        return this.http
            .post<JobFilter>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(jobFilter: JobFilter): Observable<EntityResponseType> {
        const copy = this.convert(jobFilter);
        return this.http
            .put<JobFilter>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<JobFilter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<JobFilter[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobFilter[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<JobFilter[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<JobFilter[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobFilter[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<JobFilter[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: JobFilter = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<JobFilter[]>): HttpResponse<JobFilter[]> {
        const jsonResponse: JobFilter[] = res.body;
        const body: JobFilter[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to JobFilter.
     */
    private convertItemFromServer(jobFilter: JobFilter): JobFilter {
        const copy: JobFilter = Object.assign({}, jobFilter);
        return copy;
    }

    /**
     * Convert a JobFilter to a JSON which can be sent to the server.
     */
    private convert(jobFilter: JobFilter): JobFilter {
        const copy: JobFilter = Object.assign({}, jobFilter);
        return copy;
    }
}
