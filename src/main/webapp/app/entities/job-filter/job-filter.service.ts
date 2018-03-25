import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobFilter } from './job-filter.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobFilterService {

    private resourceUrl = SERVER_API_URL + 'api/job-filters';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-filters';

    constructor(private http: Http) { }

    create(jobFilter: JobFilter): Observable<JobFilter> {
        const copy = this.convert(jobFilter);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobFilter: JobFilter): Observable<JobFilter> {
        const copy = this.convert(jobFilter);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobFilter> {
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
     * Convert a returned JSON object to JobFilter.
     */
    private convertItemFromServer(json: any): JobFilter {
        const entity: JobFilter = Object.assign(new JobFilter(), json);
        return entity;
    }

    /**
     * Convert a JobFilter to a JSON which can be sent to the server.
     */
    private convert(jobFilter: JobFilter): JobFilter {
        const copy: JobFilter = Object.assign({}, jobFilter);
        return copy;
    }
}
