import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {Job} from './job.model';
import {ResponseWrapper, createRequestOption} from '../../shared';

@Injectable()
export class JobService {

  private resourceUrl = SERVER_API_URL + 'api/jobs';
  private resourceDeActivateJobsUrl = SERVER_API_URL + 'api/deActivateJob'
  private resourceActiveJobsUrl = SERVER_API_URL + 'api/activeJobs';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/jobs';

  constructor(private http: Http, private dateUtils: JhiDateUtils) {}

  create(job: Job): Observable<Job> {
    const copy = this.convert(job);
       // console.log('Sending over wire in create '+JSON.stringify(job));
    return this.http.post(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  update(job: Job): Observable<Job> {
    const copy = this.convert(job);
   // console.log('Sending over wire in update '+JSON.stringify(job));
    return this.http.put(this.resourceUrl, copy).map((res: Response) => {
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
  
  queryActiveJobs(id: number): Observable<ResponseWrapper> {
  //  const options = createRequestOption(req);
    return this.http.get(`${this.resourceActiveJobsUrl}/${id}`)
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
    const result = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      result.push(this.convertItemFromServer(jsonResponse[i]));
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
