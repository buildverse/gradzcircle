import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {Subscriber} from 'rxjs/Subscriber';
import {JhiDateUtils} from 'ng-jhipster';
import {Candidate} from './candidate.model';
import {CandidatePublicProfile} from './candidate-public-profile.model';
import {createRequestOption} from '../../shared';
import { HttpClient, HttpResponse } from '@angular/common/http';
import {SERVER_API_URL} from '../../app.constants';

export type EntityResponseType = HttpResponse<Candidate>;
export type EntityResponseTypeCandidatePublicProfile = HttpResponse<CandidatePublicProfile>;

@Injectable()
export class CandidateService {

  private resourceUrl = SERVER_API_URL + 'api/candidates';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidates';
  private resourceUrlGetCandidateById = SERVER_API_URL+'api/candidateById';
  private imageUrl = 'api/files';
  private deleteImageUrl = 'api/remove';
  private resourceCandidateToCorporateLink = 'api/candidate-corporate-link'
  //  private resourcePublicProfileUrl ='api/candidates/public-profile';
  //  private resourcePublicProfileUrlElastic ='api/candidates/public-profile-es'
  private resourceCandidatePublicProfile = 'api/candidates/candidatePublicProfile'


  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

  create(candidate: Candidate): Observable<EntityResponseType> {
        const copy = this.convert(candidate);
        return this.http.post<Candidate>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

  update(candidate: Candidate): Observable<EntityResponseType> {
        const copy = this.convert(candidate);
        return this.http.put<Candidate>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

  linkCandidateAndCorporateForJob(candidateId: any, jobId: number,corporateId: number): Observable<EntityResponseType> {
    return this.http.get<Candidate>(`${this.resourceCandidateToCorporateLink}/${candidateId}/${jobId}/${corporateId}`, { observe: 'response'})
        .map((res: EntityResponseType) => this.convertResponse(res));
  }

  getCandidateByLoginId(id: string): Observable<EntityResponseType> {
    console.log('Calling by login id for id '+id);
    return this.http.get<Candidate>(`api/candidateByLogin/${id}`, { observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }
  
  getCandidateByCandidateId(id: string): Observable<EntityResponseType> {
    console.log('Calling my service to get candiate by id');
    return this.http.get<Candidate>(`${this.resourceUrlGetCandidateById}/${id}`, { observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  getCandidatePublicProfile(candidateId: any, jobId: number,corporateId: number): Observable<EntityResponseTypeCandidatePublicProfile> {
    return this.http.get<CandidatePublicProfile>(`${this.resourceCandidatePublicProfile}/${candidateId}/${jobId}/${corporateId}`, { observe: 'response'})
       .map((res: EntityResponseTypeCandidatePublicProfile) => this.convertResponseForPublicProfile(res));
  }


  find(id: number): Observable<EntityResponseType> {
        return this.http.get<Candidate>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

   query(req?: any): Observable<HttpResponse<Candidate[]>> {
        const options = createRequestOption(req);
        return this.http.get<Candidate[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Candidate[]>) => this.convertArrayResponse(res));
    }

   delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

  deleteImage(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.deleteImageUrl}/${id}`, { observe: 'response'});
  }

  search(req?: any): Observable<HttpResponse<Candidate[]>> {
        const options = createRequestOption(req);
        return this.http.get<Candidate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Candidate[]>) => this.convertArrayResponse(res));
    }

 private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Candidate = this.convertItemFromServer(res.body);
        return res.clone({body});
    }
  
  private convertResponseForPublicProfile(res: EntityResponseTypeCandidatePublicProfile): EntityResponseTypeCandidatePublicProfile {
        const body: CandidatePublicProfile = this.convertItemFromServerForCandidatePublicProfile(res.body);
        return res.clone({body});
    }
  
  private convertArrayResponse(res: HttpResponse<Candidate[]>): HttpResponse<Candidate[]> {
        const jsonResponse: Candidate[] = res.body;
        const body: Candidate[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

  /**
     * Convert a returned JSON object to Candidate.
     */
    private convertItemFromServer(candidate: Candidate): Candidate {
        const copy: Candidate = Object.assign({}, candidate);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateFromServer(candidate.dateOfBirth);
        return copy;
    }
  
   private convertItemFromServerForCandidatePublicProfile(candidatePublicProfile: CandidatePublicProfile): CandidatePublicProfile {
        const copy: CandidatePublicProfile = Object.assign({}, candidatePublicProfile);
        return copy;
    }


   /**
     * Convert a Candidate to a JSON which can be sent to the server.
     */
    private convert(candidate: Candidate): Candidate {
        const copy: Candidate = Object.assign({}, candidate);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(candidate.dateOfBirth);
        return copy;
    }

  private handleError(error: Response) {

    let msg = `Status code ${error.status} on url ${error.url}`;
    return Observable.throw(msg);
  }

  // getProfilePic(id: number): Observable<any> {
  //     return this.http.get(`api/files/${id}`).map(this.extractData).catch(this.handleError);
  // }

  private extractData(response: Response) {
    let body = response;
    return body || {};
  }


  // getImageData(url: string): Observable<any> {
  //         return new Observable((observer: Subscriber<any>) => {
  //             let objectUrl: string = null;
  //             this.http
  //                 .get(url, {
  //                     responseType: ResponseContentType.Blob
  //                 })
  //                 .subscribe(m => {
  //                     objectUrl = URL.createObjectURL(m.blob());
  //                     observer.next(objectUrl);
  //                 });

  //             return () => {
  //                 if (objectUrl) {
  //                     URL.revokeObjectURL(objectUrl);
  //                     objectUrl = null;
  //                 }
  //             };
  //         });
  //     }

  /*getPublicProfile(req?: any): Observable<Candidate> {
      const options = createRequestOption(req);
      return this.http.get(this.resourcePublicProfileUrl,options).map((res: Response) => {
          const jsonResponse = res.json();
         // this.convertItemFromServer(jsonResponse);
          return jsonResponse;
      });
  }*/

}
