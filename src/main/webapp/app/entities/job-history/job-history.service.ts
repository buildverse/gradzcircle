import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { JobHistory } from './job-history.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobHistoryService {

    private resourceUrl = SERVER_API_URL + 'api/job-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-histories';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(jobHistory: JobHistory): Observable<JobHistory> {
        const copy = this.convert(jobHistory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobHistory: JobHistory): Observable<JobHistory> {
        const copy = this.convert(jobHistory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobHistory> {
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
     * Convert a returned JSON object to JobHistory.
     */
    private convertItemFromServer(json: any): JobHistory {
        const entity: JobHistory = Object.assign(new JobHistory(), json);
        entity.createDate = this.dateUtils
            .convertDateTimeFromServer(json.createDate);
        entity.updateDate = this.dateUtils
            .convertDateTimeFromServer(json.updateDate);
        return entity;
    }

    /**
     * Convert a JobHistory to a JSON which can be sent to the server.
     */
    private convert(jobHistory: JobHistory): JobHistory {
        const copy: JobHistory = Object.assign({}, jobHistory);

        copy.createDate = this.dateUtils.toDate(jobHistory.createDate);

        copy.updateDate = this.dateUtils.toDate(jobHistory.updateDate);
        return copy;
    }
}
