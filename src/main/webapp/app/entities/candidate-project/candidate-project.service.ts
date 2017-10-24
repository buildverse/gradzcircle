import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SERVER_API_URL } from '../../app.constants';
import { CandidateProject } from './candidate-project.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CandidateProjectService {

    private resourceUrl = SERVER_API_URL + 'api/candidate-projects';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-projects';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(candidateProject: CandidateProject): Observable<CandidateProject> {
        const copy = this.convert(candidateProject);
        //console.log("Project in create "+ JSON.stringify(copy));
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(candidateProject: CandidateProject): Observable<CandidateProject> {
        const copy = this.convert(candidateProject);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CandidateProject> {
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

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
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
     * Convert a returned JSON object to CandidateProject.
     */
    private convertItemFromServer(json: any): CandidateProject {
        const entity: CandidateProject = Object.assign(new CandidateProject(), json);
        entity.projectStartDate = this.dateUtils
            .convertLocalDateFromServer(json.projectStartDate);
        entity.projectEndDate = this.dateUtils
            .convertLocalDateFromServer(json.projectEndDate);
        return entity;
    }

    /**
     * Convert a CandidateProject to a JSON which can be sent to the server.
     */
    private convert(candidateProject: CandidateProject): CandidateProject {
        const copy: CandidateProject = Object.assign({}, candidateProject);
        copy.projectStartDate = this.dateUtils
            .convertLocalDateToServer(candidateProject.projectStartDate);
        copy.projectEndDate = this.dateUtils
            .convertLocalDateToServer(candidateProject.projectEndDate);
        return copy;
    }
}
