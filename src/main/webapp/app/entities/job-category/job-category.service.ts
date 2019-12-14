import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JobCategory } from './job-category.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<JobCategory>;

@Injectable()
export class JobCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/job-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-categories';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/jobCategoriesBySuggest';
    constructor(private http: HttpClient) {}

    create(jobCategory: JobCategory): Observable<EntityResponseType> {
        const copy = this.convert(jobCategory);
        return this.http
            .post<JobCategory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(jobCategory: JobCategory): Observable<EntityResponseType> {
        const copy = this.convert(jobCategory);
        return this.http
            .put<JobCategory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<JobCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<JobCategory[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<JobCategory[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<JobCategory[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<JobCategory[]>) => this.convertArrayResponse(res));
    }

    searchRemote(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, { params: options, observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: JobCategory = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<JobCategory[]>): HttpResponse<JobCategory[]> {
        const jsonResponse: JobCategory[] = res.body;
        const body: JobCategory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to JobCategory.
     */
    private convertItemFromServer(jobCategory: JobCategory): JobCategory {
        const copy: JobCategory = Object.assign({}, jobCategory);
        return copy;
    }

    /**
     * Convert a JobCategory to a JSON which can be sent to the server.
     */
    private convert(jobCategory: JobCategory): JobCategory {
        const copy: JobCategory = Object.assign({}, jobCategory);
        return copy;
    }
}
