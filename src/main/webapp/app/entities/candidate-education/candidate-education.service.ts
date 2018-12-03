import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';
import {SERVER_API_URL} from '../../app.constants';
import {CandidateEducation} from './candidate-education.model';
import {createRequestOption} from '../../shared';
import {College} from '../college/college.model';
import {University} from '../university/university.model';
import {Qualification} from '../qualification/qualification.model';
import {Course} from '../course/course.model';

export type EntityResponseType = HttpResponse<CandidateEducation>;

@Injectable()
export class CandidateEducationService {

  private resourceUrl = SERVER_API_URL + 'api/candidate-educations';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-educations';
  private resourceUrlByCandidate = SERVER_API_URL + 'api/education-by-candidate';


  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(candidateEducation: CandidateEducation): Observable<EntityResponseType> {
    const copy = this.convert(candidateEducation);
    return this.http.post<CandidateEducation>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));

  }

  update(candidateEducation: CandidateEducation): Observable<EntityResponseType> {
    const copy = this.convert(candidateEducation);
    return this.http.put<CandidateEducation>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<CandidateEducation>(`${this.resourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }


  findEducationByCandidateId(id?: number): Observable<HttpResponse<CandidateEducation[]>> {
    //const options = this.createRequestOption(req);
    return this.http.get<CandidateEducation[]>(`${this.resourceUrlByCandidate}/${id}`, {observe: 'response'})
            .map((res: HttpResponse<CandidateEducation[]>) => this.convertArrayResponse(res));

  }

  query(req?: any): Observable<HttpResponse<CandidateEducation[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateEducation[]>(this.resourceUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateEducation[]>) => this.convertArrayResponse(res));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  search(req?: any): Observable<HttpResponse<CandidateEducation[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateEducation[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateEducation[]>) => this.convertArrayResponse(res));
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
    const body: CandidateEducation = this.convertItemFromServer(res.body);
    console.log('body is '+JSON.stringify(body));
    return res.clone({body});
  }

  /**
   * Convert a returned JSON object to CandidateEmployment.
   */
  private convertItemFromServer(candidateEducation: CandidateEducation): CandidateEducation {
   // console.log('kya hai ==='+JSON.stringify(candidateEducation));
    const copy: CandidateEducation = Object.assign({}, candidateEducation);
    copy.educationFromDate = this.dateUtils
      .convertLocalDateFromServer(candidateEducation.educationFromDate);
    copy.educationToDate = this.dateUtils
      .convertLocalDateFromServer(candidateEducation.educationToDate);
    this.convertEducationMetaForDisplay(copy);
    return copy;
  }

  private convertArrayResponse(res: HttpResponse<CandidateEducation[]>): HttpResponse<CandidateEducation[]> {
    const jsonResponse: CandidateEducation[] = res.body;
    const body: CandidateEducation[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  /**
   * Convert a CandidateEmployment to a JSON which can be sent to the server.
   */
  private convert(candidateEducation: CandidateEducation): CandidateEducation {
    const copy: CandidateEducation = Object.assign({}, candidateEducation);
    copy.educationFromDate = this.dateUtils
      .convertLocalDateToServer(candidateEducation.educationFromDate);
    copy.educationToDate = this.dateUtils
      .convertLocalDateToServer(candidateEducation.educationToDate);
    this.convertEducationMetaForServer(copy);
    return copy;
  }


  searchEducationOrderByToDate(req?: any): Observable<HttpResponse<CandidateEducation[]>> {
    const options = createRequestOption(req);
    return this.http.get<CandidateEducation[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<CandidateEducation[]>) => this.convertArrayResponse(res));

  }


  //CONVERT SERVER RESPONSE TO ARAY BASED FOR NGX DISPLAY

  private convertEducationMetaForDisplay(candidateEducation: CandidateEducation) {
    const collegeArray = new Array();
    collegeArray.push(candidateEducation.college);
    candidateEducation.college = collegeArray;
    const qualificationAray = new Array();
    qualificationAray.push(candidateEducation.qualification);
    candidateEducation.qualification = qualificationAray;
    const courseArray = new Array();
    courseArray.push(candidateEducation.course);
    candidateEducation.course = courseArray;
    //Manually setting these for ngb collapse on display
    candidateEducation.collapsed = true;
    if (candidateEducation.projects) {
      candidateEducation.projects.forEach((project) => {
        project.collapsed = true;
      });
    }
  }

  //CONVERT FROM VALUE: DISPLAY: TO COLLEGE OBJECT- This is required to process data at server
  private convertEducationMetaForServer(candidateEducation: CandidateEducation) {
    const college = new College();
    const university = new University();
    const qualification = new Qualification();
    const course = new Course();
    college.collegeName = candidateEducation.college[0].value;
    university.universityName = candidateEducation.college[0].university ? candidateEducation.college[0].university.universityName : '';
    qualification.qualification = candidateEducation.qualification[0].value;
    course.course = candidateEducation.course[0].value;
    candidateEducation.college.university = university;
    candidateEducation.college = college;
    candidateEducation.qualification = qualification;
    candidateEducation.course = course;
  }
}