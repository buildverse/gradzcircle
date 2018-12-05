import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {JhiDateUtils} from 'ng-jhipster';

import {Job} from './job.model';
import {CandidateList} from './candidate-list.model';
import {createRequestOption} from '../../shared';

export type EntityResponseType = HttpResponse<Job>;

@Injectable()
export class JobService {

  private resourceUrl = SERVER_API_URL + 'api/jobs';
  private resourceDeActivateJobsUrl = SERVER_API_URL + 'api/deActivateJob'
  private resourceActiveJobsUrl = SERVER_API_URL + 'api/activeJobsForCorporate';
  private resourceActiveJobsForCandidateUrl = SERVER_API_URL + 'api/newActiveJobsForCandidate';
  private resourceApplyForJobByCandidateUrl = SERVER_API_URL + 'api/applyForJob';
  private resourceMatchedCandidatedForJobUrl = SERVER_API_URL + 'api/matchedCandiatesForJob';
  private resourceShortListedCandidatedForJobUrl = SERVER_API_URL + 'api/shortListedCandidatesForJob';
  private resourceAppliedCandidatesForJobUrl = SERVER_API_URL + 'api/appliedCandiatesForJob';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/jobs';
  private resourceAppliedJobsByCandidateUrl = SERVER_API_URL + 'api/appliedJobsByCandidate';
  private resourceShortListedJobsForCandidateUrl = SERVER_API_URL + 'api/shortListedJobsForCandidate';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(job: Job): Observable<EntityResponseType> {
    const copy = this.convert(job);
    return this.http.post<Job>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  update(job: Job): Observable<EntityResponseType> {
    const copy = this.convert(job);
    return this.http.put<Job>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  applyforJob(jobId: number, loginId: number): Observable<EntityResponseType> {
    return this.http.get<Job>(`${this.resourceApplyForJobByCandidateUrl}/${jobId}/${loginId}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<Job>(`${this.resourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  query(req?: any): Observable<HttpResponse<Job[]>> {
    const options = createRequestOption(req);
    return this.http.get<Job[]>(this.resourceUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res));
  }

  queryActiveJobs(req?: any): Observable<HttpResponse<Job[]>> {
    const options = createRequestOption(req);
    return this.http.get<Job[]>(`${this.resourceActiveJobsUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res));
  }

  queryMatchedCandidatesForJob(req?: any): Observable<HttpResponse<CandidateList[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateList[]>(`${this.resourceMatchedCandidatedForJobUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res));
  }

   queryShortListedCandidatesForJob(req?: any): Observable<HttpResponse<CandidateList[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateList[]>(`${this.resourceShortListedCandidatedForJobUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res));
  }
  

  queryAppliedCandidatesForJob(req?: any): Observable<HttpResponse<CandidateList[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateList[]>(`${this.resourceAppliedCandidatesForJobUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res));
  }

  queryActiveJobsForCandidates(req: any): Observable<HttpResponse<Job[]>> {
    const options = createRequestOption(req);
    return this.http.get<Job[]>(`${this.resourceActiveJobsForCandidateUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res));
  }

  queryAppliedJobsByCandidate(req: any): Observable<HttpResponse<Job[]>> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceAppliedJobsByCandidateUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res));
  }
  
  queryShortListedJobsForCandidate(req: any): Observable<HttpResponse<Job[]>> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceShortListedJobsForCandidateUrl}/${req.id}`, {params: options, observe: 'response'})
      .map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  remove(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceDeActivateJobsUrl}/${id}`, {observe: 'response'});
  }


  search(req?: any): Observable<HttpResponse<Job[]>> {
    const options = createRequestOption(req);
    return this.http.get<Job[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<Job[]>) => this.convertJobArrayResponse(res));
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
    const body: Job = this.convertItemFromServer(res.body);
    return res.clone({body});
  }

  private convertJobArrayResponse(res: HttpResponse<Job[]>): HttpResponse<Job[]> {
    const jsonResponse: Job[] = res.body;
    const body: Job[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  private convertCandidateListArrayResponse(res: HttpResponse<CandidateList[]>): HttpResponse<CandidateList[]> {
    const jsonResponse: CandidateList[] = res.body;
    const body: CandidateList[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  /**
   * Convert a returned JSON object to Job.
   */
  private convertItemFromServer(job: Job): Job {
    const copy: Job = Object.assign({}, job);
    copy.createDate = this.dateUtils
      .convertDateTimeFromServer(job.createDate);
    copy.updateDate = this.dateUtils
      .convertDateTimeFromServer(job.updateDate);
    return copy;
  }


  /**
   * Convert a Job to a JSON which can be sent to the server.
   */
  private convert(job: Job): Job {
    const copy: Job = Object.assign({}, job);
    copy.createDate = this.dateUtils.toDate(job.createDate);
    copy.updateDate = this.dateUtils.toDate(job.updateDate);
    return copy;
  }


}
