import { Injectable } from '@angular/core';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { CandidateJob } from './candidate-job.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CandidateJob>;

@Injectable()
export class CandidateJobService {
    private resourceUrl = SERVER_API_URL + 'api/candidate-jobs';
    private resourceUrlForCandidate = SERVER_API_URL + 'api/candidateJobsByCandidate';
    private resourceUrlForCorporate = SERVER_API_URL + 'api/candidateJobsByJob';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

    queryActiveJobsForCandidates(req?: any): Observable<HttpResponse<CandidateJob[]>> {
        // const options = createRequestOption(req);
        return this.http
            .get<CandidateJob[]>(`${this.resourceUrlForCandidate}/${req.id}`, { observe: 'response' })
            .map((res: HttpResponse<CandidateJob[]>) => this.convertJobArrayResponse(res));
    }

    private convertJobArrayResponse(res: HttpResponse<CandidateJob[]>): HttpResponse<CandidateJob[]> {
        const jsonResponse: CandidateJob[] = res.body;
        const body: CandidateJob[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to Job.
     */
    private convertItemFromServer(json: any): CandidateJob {
        const entity: CandidateJob = Object.assign(new CandidateJob(), json);
        return entity;
    }

    /**
     * Convert a Job to a JSON which can be sent to the server.
     */
    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CandidateJob = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }
}
