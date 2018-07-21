import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';
import {SERVER_API_URL, ENABLE_ELASTIC} from '../../app.constants';
import {CandidateEducation} from './candidate-education.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import {College} from '../college/college.model';
import {University} from '../university/university.model';
import {Qualification} from '../qualification/qualification.model';
import {Course} from '../course/course.model';

@Injectable()
export class CandidateEducationService {

  private resourceUrl = SERVER_API_URL + 'api/candidate-educations';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-educations';
  private resourceUrlByCandidate = SERVER_API_URL + 'api/education-by-candidate';


  constructor(private http: Http, private dateUtils: JhiDateUtils) {}

  create(candidateEducation: CandidateEducation): Observable<CandidateEducation> {
    const copy = this.convert(candidateEducation);
    console.log("Seding in create" + JSON.stringify(copy));

    return this.http.post(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  update(candidateEducation: CandidateEducation): Observable<CandidateEducation> {
    const copy = this.convert(candidateEducation);
    console.log('data sent to server ' + copy);
    return this.http.put(this.resourceUrl, copy).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }

  find(id: number): Observable<CandidateEducation> {
    return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
      const jsonResponse = res.json();
      return this.convertItemFromServer(jsonResponse);
    });
  }



  findEducationByCandidateId(id?: number): Observable<ResponseWrapper> {
    //const options = this.createRequestOption(req);
    return this.http.get(`${this.resourceUrlByCandidate}/${id}`)
      .map((res: Response) => this.convertResponse(res));
  }

  query(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl, options)
      .map((res: Response) => this.convertResponse(res));
  }

  delete(id: number): Observable<Response> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  search(req?: any): Observable<ResponseWrapper> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, options)
      .map((res: any) => this.convertResponse(res));

  }

  searchEducationOrderByToDate(req?: any): Observable<ResponseWrapper> {
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
   * Convert a returned JSON object to CandidateEducation.
   */
  private convertItemFromServer(json: any): CandidateEducation {
    const entity: CandidateEducation = Object.assign(new CandidateEducation(), json);
    entity.educationFromDate = this.dateUtils
      .convertLocalDateFromServer(json.educationFromDate);
    entity.educationToDate = this.dateUtils
      .convertLocalDateFromServer(json.educationToDate);
    this.convertEducationMetaForDisplay(entity);
    console.log('After conversion ' + JSON.stringify(entity));
    return entity;
  }

  /**
   * Convert a CandidateEducation to a JSON which can be sent to the server.
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
