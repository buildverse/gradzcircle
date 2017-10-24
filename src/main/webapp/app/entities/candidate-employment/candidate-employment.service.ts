import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SERVER_API_URL } from '../../app.constants';
import { CandidateEmployment } from './candidate-employment.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CandidateEmploymentService {

    private resourceUrl = SERVER_API_URL + 'api/candidate-employments';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-employments';
    private resourceUrlByCandidate = SERVER_API_URL + 'api/employment-by-candidate'


    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(candidateEmployment: CandidateEmployment): Observable<CandidateEmployment> {
        const copy = this.convert(candidateEmployment);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(candidateEmployment: CandidateEmployment): Observable<CandidateEmployment> {
        const copy = this.convert(candidateEmployment);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CandidateEmployment> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }


    findEmploymentsByCandidateId(id?: number): Observable<ResponseWrapper> {
        //const options = this.createRequestOption(req);
        return this.http.get(`${this.resourceUrlByCandidate}/${id}`)
            .map((res: Response) => this.convertResponse(res));

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
     * Convert a returned JSON object to CandidateEmployment.
     */
    private convertItemFromServer(json: any): CandidateEmployment {
        const entity: CandidateEmployment = Object.assign(new CandidateEmployment(), json);
        entity.employmentStartDate = this.dateUtils
            .convertLocalDateFromServer(json.employmentStartDate);
        entity.employmentEndDate = this.dateUtils
            .convertLocalDateFromServer(json.employmentEndDate);
        return entity;
    }

    /**
     * Convert a CandidateEmployment to a JSON which can be sent to the server.
     */
    private convert(candidateEmployment: CandidateEmployment): CandidateEmployment {
        const copy: CandidateEmployment = Object.assign({}, candidateEmployment);
        copy.employmentStartDate = this.dateUtils
            .convertLocalDateToServer(candidateEmployment.employmentStartDate);
        copy.employmentEndDate = this.dateUtils
            .convertLocalDateToServer(candidateEmployment.employmentEndDate);
        return copy;
    }
}
