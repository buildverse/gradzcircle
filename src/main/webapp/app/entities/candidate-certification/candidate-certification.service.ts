import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';
import { CandidateCertification } from './candidate-certification.model';
import { createRequestOption } from '../../shared';
import { DATE_FORMAT } from '../../shared/constants/input.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<CandidateCertification>;

@Injectable()
export class CandidateCertificationService {
    private resourceUrl = SERVER_API_URL + 'api/candidate-certifications';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-certifications';
    private resourceSearchUrlAdmin = SERVER_API_URL + 'api/_search/candidate-certifications';
    private resourceUrlCertificationByCandidate = SERVER_API_URL + 'api/candidate-cert-by-id';

    constructor(private http: HttpClient) {}

    create(candidateCertification: CandidateCertification): Observable<EntityResponseType> {
        const copy = this.convert(candidateCertification);
        return this.http
            .post<CandidateCertification>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(candidateCertification: CandidateCertification): Observable<EntityResponseType> {
        const copy = this.convert(candidateCertification);
        return this.http
            .put<CandidateCertification>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    findCertificationsByCandidateId(id: number): Observable<HttpResponse<CandidateCertification[]>> {
        return this.http
            .get<CandidateCertification[]>(`${this.resourceUrlCertificationByCandidate}/${id}`, { observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateCertification[]>) => this.convertArrayResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CandidateCertification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<CandidateCertification[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateCertification[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateCertification[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CandidateCertification[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateCertification[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateCertification[]>) => this.convertArrayResponse(res)));
    }

    searchForAdmin(req?: any): Observable<HttpResponse<CandidateCertification[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateCertification[]>(this.resourceSearchUrlAdmin, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateCertification[]>) => this.convertArrayResponse(res)));
    }

    private convertArrayResponse(res: HttpResponse<CandidateCertification[]>): HttpResponse<CandidateCertification[]> {
        const jsonResponse: CandidateCertification[] = res.body;
        const body: CandidateCertification[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CandidateCertification.
     */
    private convertItemFromServer(candidateCertification: CandidateCertification): CandidateCertification {
        const copy: CandidateCertification = Object.assign({}, candidateCertification);
        if (candidateCertification.certificationDate) {
            copy.certificationDate = moment(candidateCertification.certificationDate);
        }
        copy.collapsed = true;
        return copy;
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CandidateCertification = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    /**
     * Convert a CandidateCertification to a JSON which can be sent to the server.
     */
    private convert(candidateCertification: CandidateCertification): CandidateCertification {
        const copy: CandidateCertification = Object.assign({}, candidateCertification);
        if (candidateCertification.certificationDate) {
            copy.certificationDate = candidateCertification.certificationDate.format(DATE_FORMAT);
        }

        return copy;
    }
}
