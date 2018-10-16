import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {Job} from './job.model';
import {CandidateList} from './candidate-list.model';
import {ResponseWrapper, createRequestOption} from '../../shared';


@Injectable()
export class JobService {

  private resourceUrl = SERVER_API_URL + 'api/jobs';
  private resourceDeActivateJobsUrl = SERVER_API_URL + 'api/deActivateJob'
  private resourceActiveJobsUrl = SERVER_API_URL + 'api/activeJobsForCorporate';
  private resourceActiveJobsForCandidateUrl = SERVER_API_URL + 'api/newActiveJobsForCandidate';
  private resourceApplyForJobByCandidateUrl = SERVER_API_URL + 'api/applyForJob';
  private resourceMatchedCandidatedForJobUrl = SERVER_API_URL + 'api/matchedCandiatesForJob';
  private resourceAppliedCandidatesForJobUrl = SERVER_API_URL + 'api/appliedCandiatesForJob';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/jobs';
  private resourceAppliedJobsByCandidateUrl = SERVER_API_URL + 'api/appliedJobsByCandidate';

  constructor(private http: Http, private dateUtils: JhiDateUtils) {}

  create(job: Job): Observable<Job> {
    const copy = this.convert(job);
    return this.http.post(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  update(job: Job): Observable<Job> {
    const copy = this.convert(job);
    return this.http.put(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  applyforJob(jobId: number, loginId: number): Observable<Job> {
    return this.http.get(`${this.resourceApplyForJobByCandidateUrl}/${jobId}/${loginId}`).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  find(id: number): Observable<Job> {
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

  queryActiveJobs(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceActiveJobsUrl}/${req.id}`, options)
      .map((res: Response) => this.convertResponse(res));
  }

  queryMatchedCandidatesForJob(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceMatchedCandidatedForJobUrl}/${req.id}`, options)
      .map((res: Response) => this.convertCandidateListResponse(res));
  }

  queryAppliedCandidatesForJob(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceAppliedCandidatesForJobUrl}/${req.id}`, options)
      .map((res: Response) => this.convertCandidateListResponse(res));
  }

  queryActiveJobsForCandidates(req: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceActiveJobsForCandidateUrl}/${req.id}`, options)
      .map((res: Response) => this.convertResponse(res));
  }

  queryAppliedJobsByCandidate(req: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceAppliedJobsByCandidateUrl}/${req.id}`, options)
      .map((res: Response) => this.convertResponse(res));
  }

  delete(id: number): Observable<Response> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  remove(id: number): Observable<Response> {
    return this.http.delete(`${this.resourceDeActivateJobsUrl}/${id}`);
  }


  search(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, options)
      .map((res: any) => this.convertResponse(res));
  }

  private convertResponse(res: Response): ResponseWrapper {
    const jsonResponse = res.json();
    // console.log('esponse from server is '+JSON.stringify(jsonResponse));
    const result = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      result.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return new ResponseWrapper(res.headers, result, res.status);
  }

  private convertCandidateListResponse(res: Response): ResponseWrapper {
    const jsonResponse = res.json();
    // console.log('esponse from server is '+JSON.stringify(jsonResponse));
    const result = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      const entity: CandidateList = Object.assign(new CandidateList(), jsonResponse[i]);
      result.push(entity);
    }
    return new ResponseWrapper(res.headers, result, res.status);
  }



  /**
   * Convert a returned JSON object to Job.
   */
  private convertItemFromServer(json: any): Job {
    const entity: Job = Object.assign(new Job(), json);
    entity.createDate = this.dateUtils
      .convertDateTimeFromServer(json.createDate);
    entity.updateDate = this.dateUtils
      .convertDateTimeFromServer(json.updateDate);
    return entity;
  }

  /**
   * Convert a Job to a JSON which can be sent to the server.
   */
  private convert(job: Job): Job {
    const copy: Job = Object.assign({}, job);
    copy.createDate = this.dateUtils
      .toDate(job.createDate);
    copy.updateDate = this.dateUtils
      .toDate(job.updateDate);

    return copy;
  }


}
