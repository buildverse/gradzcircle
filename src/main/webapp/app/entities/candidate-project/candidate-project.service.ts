import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CandidateProject } from './candidate-project.model';
import { createRequestOption } from '../../shared';
import { DATE_FORMAT } from '../../shared/constants/input.constants';
import * as moment from 'moment';
export type EntityResponseType = HttpResponse<CandidateProject>;

@Injectable()
export class CandidateProjectService {
    private resourceUrl = SERVER_API_URL + 'api/candidate-projects';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-projects';

    constructor(private http: HttpClient) {}

    create(candidateProject: CandidateProject): Observable<EntityResponseType> {
        const copy = this.convert(candidateProject);
        return this.http
            .post<CandidateProject>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(candidateProject: CandidateProject): Observable<EntityResponseType> {
        const copy = this.convert(candidateProject);
        return this.http
            .put<CandidateProject>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CandidateProject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CandidateProject[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateProject[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CandidateProject[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CandidateProject[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateProject[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CandidateProject[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CandidateProject = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<CandidateProject[]>): HttpResponse<CandidateProject[]> {
        const jsonResponse: CandidateProject[] = res.body;
        const body: CandidateProject[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CandidateProject.
     */
    private convertItemFromServer(candidateProject: CandidateProject): CandidateProject {
        const copy: CandidateProject = Object.assign({}, candidateProject);
        if (candidateProject.projectStartDate) {
            copy.projectStartDate = moment(candidateProject.projectStartDate);
        }
        if (candidateProject.projectEndDate) {
            copy.projectEndDate = moment(candidateProject.projectEndDate);
        }

        return copy;
    }

    /**
     * Convert a CandidateProject to a JSON which can be sent to the server.
     */
    private convert(candidateProject: CandidateProject): CandidateProject {
        const copy: CandidateProject = Object.assign({}, candidateProject);
        if (candidateProject.projectStartDate) {
            copy.projectStartDate = candidateProject.projectStartDate.format(DATE_FORMAT);
        }
        if (candidateProject.projectEndDate) {
            copy.projectEndDate = candidateProject.projectEndDate.format(DATE_FORMAT);
        }

        return copy;
    }
}
