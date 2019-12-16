import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { JobType } from './job-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<JobType>;

@Injectable()
export class JobTypeService {
    private resourceUrl = SERVER_API_URL + 'api/job-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-types';

    constructor(private http: HttpClient) {}

    create(jobType: JobType): Observable<EntityResponseType> {
        const copy = this.convert(jobType);
        return this.http
            .post<JobType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(jobType: JobType): Observable<EntityResponseType> {
        const copy = this.convert(jobType);
        return this.http
            .put<JobType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<JobType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<JobType[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<JobType[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<JobType[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<JobType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<JobType[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: JobType = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<JobType[]>): HttpResponse<JobType[]> {
        const jsonResponse: JobType[] = res.body;
        const body: JobType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to JobType.
     */
    private convertItemFromServer(jobType: JobType): JobType {
        const copy: JobType = Object.assign({}, jobType);
        return copy;
    }

    /**
     * Convert a JobType to a JSON which can be sent to the server.
     */
    private convert(jobType: JobType): JobType {
        const copy: JobType = Object.assign({}, jobType);
        return copy;
    }
}
