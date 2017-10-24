import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Skills } from './skills.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SkillsService {

    private resourceUrl = SERVER_API_URL + 'api/skills';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/skills';

    constructor(private http: Http) { }

    create(skills: Skills): Observable<Skills> {
        const copy = this.convert(skills);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(skills: Skills): Observable<Skills> {
        const copy = this.convert(skills);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Skills> {
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
     * Convert a returned JSON object to Skills.
     */
    private convertItemFromServer(json: any): Skills {
        const entity: Skills = Object.assign(new Skills(), json);
        return entity;
    }

    /**
     * Convert a Skills to a JSON which can be sent to the server.
     */
    private convert(skills: Skills): Skills {
        const copy: Skills = Object.assign({}, skills);
        return copy;
    }
}
