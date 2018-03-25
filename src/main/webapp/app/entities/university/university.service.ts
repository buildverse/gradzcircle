import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { University } from './university.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UniversityService {

    private resourceUrl = SERVER_API_URL + 'api/universities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/universities';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/universitiesBySuggest';

    constructor(private http: Http) { }

    create(university: University): Observable<University> {
        const copy = this.convert(university);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(university: University): Observable<University> {
        const copy = this.convert(university);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<University> {
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

    searchRemote(req?: any):Observable<Response>{
        const options = createRequestOption(req);
       // console.log("Request query is "+req);
       // console.log("options "+JSON.stringify(options));
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
     * Convert a returned JSON object to University.
     */
    private convertItemFromServer(json: any): University {
        const entity: University = Object.assign(new University(), json);
        return entity;
    }

    /**
     * Convert a University to a JSON which can be sent to the server.
     */
    private convert(university: University): University {
        const copy: University = Object.assign({}, university);
        return copy;
    }
}
