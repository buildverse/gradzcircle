import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SERVER_API_URL, ENABLE_ELASTIC } from '../../app.constants';
import { CandidateEducation } from './candidate-education.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CandidateEducationService {

    private resourceUrl = SERVER_API_URL + 'api/candidate-educations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-educations';
    private resourceUrlByCandidate = SERVER_API_URL + 'api/education-by-candidate';


    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(candidateEducation: CandidateEducation): Observable<CandidateEducation> {
        const copy = this.convert(candidateEducation);
        // console.log("Seding in create" + JSON.stringify(copy));

        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(candidateEducation: CandidateEducation): Observable<CandidateEducation> {
        const copy = this.convert(candidateEducation);
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
        return copy;
    }
}
