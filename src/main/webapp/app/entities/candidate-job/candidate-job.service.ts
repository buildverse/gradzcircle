import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {CandidateJob} from './candidate-job.model';
import {ResponseWrapper, createRequestOption} from '../../shared';

@Injectable()
export class CandidateJobService {
  private resourceUrl = SERVER_API_URL + 'api/candidate-jobs';
  private resourceUrlForCandidate = SERVER_API_URL + 'api/candidateJobsByCandidate';
  private resourceUrlForCorporate = SERVER_API_URL + 'api/candidateJobsByJob';

  constructor(private http: Http, private dateUtils: JhiDateUtils) {}


  queryActiveJobsForCandidates(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceUrlForCandidate}/${req.id}`, options)
      .map((res: Response) => this.convertResponse(res));
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
  private convert(candidateJob: CandidateJob): CandidateJob {
    const copy: CandidateJob = Object.assign({}, candidateJob);
    return copy;
  }

}