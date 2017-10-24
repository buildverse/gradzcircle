import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CandidateNonAcademicWorkService {

    private resourceUrl = SERVER_API_URL + 'api/candidate-non-academic-works';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-non-academic-works';
    private resourceUrlNonAcademicWorkByCandidate = SERVER_API_URL + 'api/candidate-non-academic-work-by-id';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(candidateNonAcademicWork: CandidateNonAcademicWork): Observable<CandidateNonAcademicWork> {
        const copy = this.convert(candidateNonAcademicWork);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(candidateNonAcademicWork: CandidateNonAcademicWork): Observable<CandidateNonAcademicWork> {
        const copy = this.convert(candidateNonAcademicWork);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    findNonAcademicWorkByCandidateId(id: number): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrlNonAcademicWorkByCandidate}/${id}`)
          .map((res: Response) => this.convertResponse(res));
  }

    find(id: number): Observable<CandidateNonAcademicWork> {
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
     * Convert a returned JSON object to CandidateNonAcademicWork.
     */
    private convertItemFromServer(json: any): CandidateNonAcademicWork {
        const entity: CandidateNonAcademicWork = Object.assign(new CandidateNonAcademicWork(), json);
        entity.nonAcademicWorkStartDate = this.dateUtils
            .convertLocalDateFromServer(json.nonAcademicWorkStartDate);
        entity.nonAcademicWorkEndDate = this.dateUtils
            .convertLocalDateFromServer(json.nonAcademicWorkEndDate);
        return entity;
    }

    /**
     * Convert a CandidateNonAcademicWork to a JSON which can be sent to the server.
     */
    private convert(candidateNonAcademicWork: CandidateNonAcademicWork): CandidateNonAcademicWork {
        const copy: CandidateNonAcademicWork = Object.assign({}, candidateNonAcademicWork);
        copy.nonAcademicWorkStartDate = this.dateUtils
            .convertLocalDateToServer(candidateNonAcademicWork.nonAcademicWorkStartDate);
        copy.nonAcademicWorkEndDate = this.dateUtils
            .convertLocalDateToServer(candidateNonAcademicWork.nonAcademicWorkEndDate);
        return copy;
    }
}
