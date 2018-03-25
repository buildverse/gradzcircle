import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';
import { of } from 'rxjs/observable/of';
import { College } from './college.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CollegeService {

    private resourceUrl = SERVER_API_URL + 'api/colleges';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/colleges';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/collegesBySuggest';

    constructor(private http: Http) { }

    create(college: College): Observable<College> {
        const copy = this.convert(college);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(college: College): Observable<College> {
        const copy = this.convert(college);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<College> {
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

    searchRemote(req?: any): Observable<Response> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, options);

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
     * Convert a returned JSON object to College.
     */
    private convertItemFromServer(json: any): College {
        const entity: College = Object.assign(new College(), json);
        return entity;
    }

    /**
     * Convert a College to a JSON which can be sent to the server.
     */
    private convert(college: College): College {
        const copy: College = Object.assign({}, college);
        return copy;
    }
}
