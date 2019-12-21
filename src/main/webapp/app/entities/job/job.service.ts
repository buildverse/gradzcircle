import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { JhiDateUtils } from 'ng-jhipster';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { Job } from './job.model';
import { CandidateList } from './candidate-list.model';
import { createRequestOption } from '../../shared';
import * as moment from 'moment';
import { map } from 'rxjs/operators';
export type EntityResponseType = HttpResponse<Job>;

@Injectable()
export class JobService {
    private resourceUrl = SERVER_API_URL + 'api/jobs';
    private resourceDeActivateJobsUrl = SERVER_API_URL + 'api/deActivateJob';
    private resourceActiveJobsByCorporateUrl = SERVER_API_URL + 'api/activeJobsForCorporate';
    private resourceActiveJobs = SERVER_API_URL + 'api/activeJobs';
    private resourceActiveJobsForCandidateUrl = SERVER_API_URL + 'api/newActiveJobsForCandidate';
    private resourceApplyForJobByCandidateUrl = SERVER_API_URL + 'api/applyForJob';
    private resourceMatchedCandidatedForJobUrl = SERVER_API_URL + 'api/matchedCandiatesForJob';
    private resourceShortListedCandidatedForJobUrl = SERVER_API_URL + 'api/shortListedCandidatesForJob';
    private resourceAppliedCandidatesForJobUrl = SERVER_API_URL + 'api/appliedCandiatesForJob';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/jobs';
    private resourceAppliedJobsByCandidateUrl = SERVER_API_URL + 'api/appliedJobsByCandidate';
    private resourceShortListedJobsForCandidateUrl = SERVER_API_URL + 'api/shortListedJobsForCandidate';
    private resourceGetJobsByEmploymentTypeOnly = SERVER_API_URL + 'api/activeJobsByEmploymentType';
    private resourceGetJobsByEmploymentTypeAndOneJobType = SERVER_API_URL + 'api/activeJobsByOneEmploymentTypeAndOneJobType';
    private resourceGetJobsByEmploymentTypeAndTwoJobTypes = SERVER_API_URL + 'api/activeJobsByOneEmploymentTypeAndTwoJobType';
    private resourceGetJobsByEmploymentTypeAndThreeJobTypes = SERVER_API_URL + 'api/activeJobsByOneEmploymentTypeAndThreeJobType';
    private resourceGetJobsByOneJobType = SERVER_API_URL + 'api/activeJobsByOneJobType';
    private resourceGetJobsByTwoJobTypes = SERVER_API_URL + 'api/activeJobsByTwoJobTypes';
    private resourceGetJobsByThreeJobTypes = SERVER_API_URL + 'api/activeJobsByThreeJobTypes';
    private resourceGetActiveJobCount = SERVER_API_URL + 'api/countOfActiveJobs';
    private resourceGetJobListForLinkedCandidate = SERVER_API_URL + 'api/jobListForCandidateShortlistedByCorporate';
    private resourceGetJobEconomics = SERVER_API_URL + 'api/jobForAddingCandidates';
    private resourceAdditionalCandidatesToJob = SERVER_API_URL + 'api/addCandidatesToJob';
    private resourceViewJobForCandidate = SERVER_API_URL + 'api/viewJobForCandidate';

    constructor(private http: HttpClient) {}

    create(job: Job): Observable<EntityResponseType> {
        const copy = this.convert(job);
        return this.http
            .post<Job>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(job: Job): Observable<EntityResponseType> {
        const copy = this.convert(job);
        //  console.log('Am seding what as date ' + JSON.stringify(copy));
        return this.http
            .put<Job>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    addtionalCandidatesForJob(job: Job): Observable<EntityResponseType> {
        const copy = this.convert(job);
        return this.http
            .put<Job>(this.resourceAdditionalCandidatesToJob, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    applyforJob(jobId: number, loginId: number): Observable<EntityResponseType> {
        return this.http
            .get<Job>(`${this.resourceApplyForJobByCandidateUrl}/${jobId}/${loginId}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Job>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    getJobForCandidateView(jobId: number, candidateId: number): Observable<EntityResponseType> {
        return this.http
            .get<Job>(`${this.resourceViewJobForCandidate}/${jobId}/${candidateId}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    getJobEconomics(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Job>(`${this.resourceGetJobEconomics}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryForActiveJobCount(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(this.resourceGetActiveJobCount, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<any>) => res.body));
    }

    queryActiveJobsByCorporate(req?: any): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(`${this.resourceActiveJobsByCorporateUrl}/${req.id}`, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByEmploymentType(
        req?: any,
        employmentType?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(`${this.resourceGetJobsByEmploymentTypeOnly}/${employmentType}/${id}/${matchScoreFrom}/${matchScoreTo}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByOneJobType(
        req?: any,
        jobType?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(`${this.resourceGetJobsByOneJobType}/${jobType}/${id}/${matchScoreFrom}/${matchScoreTo}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByTwoJobTypes(
        req?: any,
        jobType1?: string,
        jobType2?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(`${this.resourceGetJobsByTwoJobTypes}/${jobType1}/${jobType2}/${id}/${matchScoreFrom}/${matchScoreTo}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByThreeJobTypes(
        req?: any,
        jobType1?: string,
        jobType2?: string,
        jobType3?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(
                `${this.resourceGetJobsByThreeJobTypes}/${jobType1}/${jobType2}/${jobType3}
            /${id}/${matchScoreFrom}/${matchScoreTo}`,
                { params: options, observe: 'response' }
            )
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByEmploymentTypeAndOneJobType(
        req?: any,
        employmentType?: string,
        jobType?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(
                `${this.resourceGetJobsByEmploymentTypeAndOneJobType}/${employmentType}/${jobType}
              /${id}/${matchScoreFrom}/${matchScoreTo}`,
                { params: options, observe: 'response' }
            )
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByEmploymentTypeAndTwoJobTypes(
        req?: any,
        employmentType?: string,
        jobType1?: string,
        jobType2?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(
                `${this.resourceGetJobsByEmploymentTypeAndTwoJobTypes}/${employmentType}/${jobType1}/
                  ${jobType2}/${id}/${matchScoreFrom}/${matchScoreTo}`,
                { params: options, observe: 'response' }
            )
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobsByEmploymentTypeAndThreeJobTypes(
        req?: any,
        employmentType?: string,
        jobType1?: string,
        jobType2?: string,
        jobType3?: string,
        id?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(
                `${this.resourceGetJobsByEmploymentTypeAndThreeJobTypes}/${employmentType}/${jobType1}/${jobType2}/${jobType3}
                    /${id}/${matchScoreFrom}/${matchScoreTo}`,
                { params: options, observe: 'response' }
            )
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryActiveJobs(req?: any): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(`${this.resourceActiveJobs}`, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryMatchedCandidatesForJob(
        req?: any,
        fromMatchScore?: number,
        toMatchScore?: number,
        reviewed?: boolean
    ): Observable<HttpResponse<CandidateList[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateList[]>(`${this.resourceMatchedCandidatedForJobUrl}/${req.id}/${fromMatchScore}/${toMatchScore}/${reviewed}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res)));
    }

    queryShortListedCandidatesForJob(req?: any): Observable<HttpResponse<CandidateList[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateList[]>(`${this.resourceShortListedCandidatedForJobUrl}/${req.id}`, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res)));
    }

    queryAppliedCandidatesForJob(req?: any, fromMatchScore?: number, toMatchScore?: number): Observable<HttpResponse<CandidateList[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateList[]>(`${this.resourceAppliedCandidatesForJobUrl}/${req.id}/${fromMatchScore}/${toMatchScore}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res)));
    }

    queryActiveJobsForCandidates(
        req?: any,
        candidateId?: number,
        matchScoreFrom?: number,
        matchScoreTo?: number
    ): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(`${this.resourceActiveJobsForCandidateUrl}/${candidateId}/${matchScoreFrom}/${matchScoreTo}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryJobListForLinkedCandidates(req?: any, candidateId?: number, corporateId?: number): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        // console.log('Candidate and corp id for request are ' + candidateId + ' ' + corporateId);
        return this.http
            .get<Job[]>(`${this.resourceGetJobListForLinkedCandidate}/${candidateId}/${corporateId}`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryAppliedJobsByCandidate(req?: any): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get(`${this.resourceAppliedJobsByCandidateUrl}/${req.id}`, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    queryShortListedJobsForCandidate(req?: any): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get(`${this.resourceShortListedJobsForCandidateUrl}/${req.id}`, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    remove(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceDeActivateJobsUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<Job[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Job[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Job = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertJobArrayResponse(res: HttpResponse<Job[]>): HttpResponse<Job[]> {
        const jsonResponse: Job[] = res.body;
        const body: Job[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    private convertCandidateListArrayResponse(res: HttpResponse<CandidateList[]>): HttpResponse<CandidateList[]> {
        const jsonResponse: CandidateList[] = res.body;
        const body: CandidateList[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(jsonResponse[i]);
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to Job.
     */
    private convertItemFromServer(job: Job): Job {
        const copy: Job = Object.assign({}, job);
        copy.createDate = moment(job.createDate);
        copy.updateDate = moment(job.updateDate);
        return copy;
    }

    /**
     * Convert a Job to a JSON which can be sent to the server.
     */
    private convert(job: Job): Job {
        const copy: Job = Object.assign({}, job);
        copy.createDate = job.createDate ? job.createDate.format(DATE_FORMAT) : job.createDate;
        copy.updateDate = job.updateDate ? job.updateDate.format(DATE_FORMAT) : job.updateDate;
        return copy;
    }
}
