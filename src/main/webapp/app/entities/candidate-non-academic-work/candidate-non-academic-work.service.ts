import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { createRequestOption } from '../../shared';
import { DATE_FORMAT } from '../../shared/constants/input.constants';
import * as moment from 'moment';
export type EntityResponseType = HttpResponse<CandidateNonAcademicWork>;

@Injectable()
export class CandidateNonAcademicWorkService {
    private resourceUrl = SERVER_API_URL + 'api/candidate-non-academic-works';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-non-academic-works';
    private resourceUrlNonAcademicWorkByCandidate = SERVER_API_URL + 'api/candidate-non-academic-work-by-id';

    constructor(private http: HttpClient) {}

    create(candidateNonAcademicWork: CandidateNonAcademicWork): Observable<EntityResponseType> {
        const copy = this.convert(candidateNonAcademicWork);
        return this.http
            .post<CandidateNonAcademicWork>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(candidateNonAcademicWork: CandidateNonAcademicWork): Observable<EntityResponseType> {
        const copy = this.convert(candidateNonAcademicWork);
        return this.http
            .put<CandidateNonAcademicWork>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    findNonAcademicWorkByCandidateId(id: number): Observable<HttpResponse<CandidateNonAcademicWork[]>> {
        return this.http
            .get<CandidateNonAcademicWork[]>(`${this.resourceUrlNonAcademicWorkByCandidate}/${id}`, { observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateNonAcademicWork[]>) => this.convertArrayResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CandidateNonAcademicWork>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<CandidateNonAcademicWork[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateNonAcademicWork[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateNonAcademicWork[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CandidateNonAcademicWork[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateNonAcademicWork[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateNonAcademicWork[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CandidateNonAcademicWork = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<CandidateNonAcademicWork[]>): HttpResponse<CandidateNonAcademicWork[]> {
        const jsonResponse: CandidateNonAcademicWork[] = res.body;
        const body: CandidateNonAcademicWork[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CandidateNonAcademicWork.
     */
    private convertItemFromServer(candidateNonAcademicWork: CandidateNonAcademicWork): CandidateNonAcademicWork {
        const copy: CandidateNonAcademicWork = Object.assign({}, candidateNonAcademicWork);
        if (candidateNonAcademicWork.nonAcademicWorkStartDate) {
            copy.nonAcademicWorkStartDate = moment(candidateNonAcademicWork.nonAcademicWorkStartDate);
        }

        if (candidateNonAcademicWork.nonAcademicWorkEndDate) {
            copy.nonAcademicWorkEndDate = moment(candidateNonAcademicWork.nonAcademicWorkEndDate);
        }
        copy.collapsed = true;
        return copy;
    }

    /**
     * Convert a CandidateNonAcademicWork to a JSON which can be sent to the server.
     */
    private convert(candidateNonAcademicWork: CandidateNonAcademicWork): CandidateNonAcademicWork {
        const copy: CandidateNonAcademicWork = Object.assign({}, candidateNonAcademicWork);
        if (candidateNonAcademicWork.nonAcademicWorkStartDate) {
            copy.nonAcademicWorkStartDate = candidateNonAcademicWork.nonAcademicWorkStartDate.format(DATE_FORMAT);
        }
        if (candidateNonAcademicWork.nonAcademicWorkEndDate) {
            copy.nonAcademicWorkEndDate = candidateNonAcademicWork.nonAcademicWorkEndDate.format(DATE_FORMAT);
        }
        return copy;
    }
}
