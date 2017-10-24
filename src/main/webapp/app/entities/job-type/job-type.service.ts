import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobType } from './job-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobTypeService {

    private resourceUrl = SERVER_API_URL + 'api/job-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-types';

    constructor(private http: Http) { }

    create(jobType: JobType): Observable<JobType> {
        const copy = this.convert(jobType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobType: JobType): Observable<JobType> {
        const copy = this.convert(jobType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobType> {
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
     * Convert a returned JSON object to JobType.
     */
    private convertItemFromServer(json: any): JobType {
        const entity: JobType = Object.assign(new JobType(), json);
        return entity;
    }

    /**
     * Convert a JobType to a JSON which can be sent to the server.
     */
    private convert(jobType: JobType): JobType {
        const copy: JobType = Object.assign({}, jobType);
        return copy;
    }
}
