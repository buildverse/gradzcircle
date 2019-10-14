import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {JhiDateUtils} from 'ng-jhipster';
import {Candidate} from './candidate.model';
import {CandidatePublicProfile} from './candidate-public-profile.model';
import {createRequestOption} from '../../shared';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from '../../app.constants';
import {Country} from '../../entities/country/country.model';
import {Nationality} from '../../entities/nationality/nationality.model';
import { CandidateList } from '../job/candidate-list.model';

export type EntityResponseType = HttpResponse<Candidate>;
export type EntityResponseTypeCandidatePublicProfile = HttpResponse<CandidatePublicProfile>;

@Injectable()
export class CandidateService {

  private resourceUrl = SERVER_API_URL + 'api/candidates';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidates';
  private resourceUrlGetCandidateById = SERVER_API_URL + 'api/candidateById';
  private resourceUrlGetCandidateDetails = SERVER_API_URL + 'api/candidateDetails';
  private deleteImageUrl = 'api/remove';
  private resourceUrlGuest ='api/candidatesPreview';
  private resourceCandidateToCorporateLink = 'api/candidate-corporate-link';
  private resourceCandidatePublicProfile = 'api/candidates/candidatePublicProfile';


  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(candidate: Candidate): Observable<EntityResponseType> {
    const copy = this.convert(candidate);
    return this.http.post<Candidate>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  update(candidate: Candidate): Observable<EntityResponseType> {
    const copy = this.convert(candidate);
    return this.http.put<Candidate>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  linkCandidateAndCorporateForJob(candidateId: any, jobId: number, corporateId: number): Observable<EntityResponseType> {
    return this.http.get<Candidate>(`${this.resourceCandidateToCorporateLink}/${candidateId}/${jobId}/${corporateId}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  getCandidateByLoginId(id: string): Observable<EntityResponseType> {
    return this.http.get<Candidate>(`api/candidateByLogin/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  getCandidateByCandidateId(id: string): Observable<EntityResponseType> {
    return this.http.get<Candidate>(`${this.resourceUrlGetCandidateById}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }
  
   getCandidateDetails(id: string): Observable<EntityResponseType> {
    return this.http.get<Candidate>(`${this.resourceUrlGetCandidateDetails}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  getCandidatePublicProfile(candidateId: any, jobId: number, corporateId: number): Observable<EntityResponseTypeCandidatePublicProfile> {
    return this.http.get<CandidatePublicProfile>(`${this.resourceCandidatePublicProfile}/${candidateId}/${jobId}/${corporateId}`, {observe: 'response'})
      .map((res: EntityResponseTypeCandidatePublicProfile) => this.convertResponseForPublicProfile(res));
  }


  find(id: number): Observable<EntityResponseType> {
    return this.http.get<Candidate>(`${this.resourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  query(req?: any): Observable<HttpResponse<Candidate[]>> {
    const options = createRequestOption(req);
    return this.http.get<Candidate[]>(this.resourceUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<Candidate[]>) => this.convertArrayResponse(res));
  }
  
   queryForGuest(req?: any): Observable<HttpResponse<CandidateList[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateList[]>(this.resourceUrlGuest, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateList[]>) => this.convertCandidateListArrayResponse(res));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  deleteImage(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.deleteImageUrl}/${id}`, {observe: 'response'});
  }

  search(req?: any): Observable<HttpResponse<Candidate[]>> {
    const options = createRequestOption(req);
    return this.http.get<Candidate[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
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

   private convertCandidateListArrayResponse(res: HttpResponse<CandidateList[]>): HttpResponse<CandidateList[]> {
    const jsonResponse: CandidateList[] = res.body;
    const body: CandidateList[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(jsonResponse[i]);
    }
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
     this.convertMetaDataForDisplay(copy);
    return copy;
  }

  private convertMetaDataForDisplay(candidate: Candidate) {
    if (candidate.addresses && candidate.addresses.length > 0) {
     const countryArray = new Array();
      countryArray.push(candidate.addresses[0].country);
      candidate.addresses[0].country = countryArray;
    }
    if (candidate.nationality && candidate.nationality) {
      const nationalityArray = new Array();
      nationalityArray.push(candidate.nationality);
      candidate.nationality = nationalityArray;
    }
  }
  
  
  private convertItemFromServerForCandidatePublicProfile(candidatePublicProfile: CandidatePublicProfile): CandidatePublicProfile {
    const copy: CandidatePublicProfile = Object.assign({}, candidatePublicProfile);
    if (copy.educations) {
      copy.educations.forEach((education) => {
        education.collapsed = true;
        if (education.projects) {
          education.projects.forEach((project) => {
            project.collapsed = true;
          });
        }
      });
    }
    if (copy.certifications) {
      copy.certifications.forEach((certification) => certification.collapsed = true);
    }
    if (copy.employments) {
      copy.employments.forEach((employment) => {
        employment.collapsed = true;
        if (employment.projects) {
          employment.projects.forEach((project) => {
            project.collapsed = true;
          });
        }
      });
    }
    if (copy.nonAcademics) {
      copy.nonAcademics.forEach((nonAcademic) => nonAcademic.collapsed = true);
    }
    return copy;
  }


  /**
    * Convert a Candidate to a JSON which can be sent to the server.
    */
  private convert(candidate: Candidate): Candidate {
    const copy: Candidate = Object.assign({}, candidate);
    copy.dateOfBirth = this.dateUtils
      .convertLocalDateToServer(candidate.dateOfBirth);
    this.convertMetaDataForServer(copy); 
        return copy;
  }

  convertMetaDataForServer(candidate: Candidate) {
    const nationality = new Nationality();
    const country = new Country();
    if (candidate.nationality) {
      nationality.nationality = candidate.nationality[0].value;
      candidate.nationality = nationality;
    }
    if (candidate.addresses && candidate.addresses[0] && candidate.addresses[0].country[0]) {
      country.countryNiceName = candidate.addresses[0].country[0].value;
      candidate.addresses[0].country = country;
    }
  }

  private handleError(error: Response) {

    const msg = `Status code ${error.status} on url ${error.url}`;
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
