import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobFilterHistory } from './job-filter-history.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobFilterHistoryService {

    private resourceUrl = SERVER_API_URL + 'api/job-filter-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-filter-histories';

    constructor(private http: Http) { }

    create(jobFilterHistory: JobFilterHistory): Observable<JobFilterHistory> {
        const copy = this.convert(jobFilterHistory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobFilterHistory: JobFilterHistory): Observable<JobFilterHistory> {
        const copy = this.convert(jobFilterHistory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobFilterHistory> {
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
     * Convert a returned JSON object to JobFilterHistory.
     */
    private convertItemFromServer(json: any): JobFilterHistory {
        const entity: JobFilterHistory = Object.assign(new JobFilterHistory(), json);
        return entity;
    }

    /**
     * Convert a JobFilterHistory to a JSON which can be sent to the server.
     */
    private convert(jobFilterHistory: JobFilterHistory): JobFilterHistory {
        const copy: JobFilterHistory = Object.assign({}, jobFilterHistory);
        return copy;
    }
}
