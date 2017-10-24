import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobCategory } from './job-category.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobCategoryService {

    private resourceUrl = SERVER_API_URL + 'api/job-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-categories';

    constructor(private http: Http) { }

    create(jobCategory: JobCategory): Observable<JobCategory> {
        const copy = this.convert(jobCategory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobCategory: JobCategory): Observable<JobCategory> {
        const copy = this.convert(jobCategory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobCategory> {
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
     * Convert a returned JSON object to JobCategory.
     */
    private convertItemFromServer(json: any): JobCategory {
        const entity: JobCategory = Object.assign(new JobCategory(), json);
        return entity;
    }

    /**
     * Convert a JobCategory to a JSON which can be sent to the server.
     */
    private convert(jobCategory: JobCategory): JobCategory {
        const copy: JobCategory = Object.assign({}, jobCategory);
        return copy;
    }
}
