import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Nationality } from './nationality.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class NationalityService {

    private resourceUrl = SERVER_API_URL + 'api/nationalities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/nationalities';

    constructor(private http: Http) { }

    create(nationality: Nationality): Observable<Nationality> {
        const copy = this.convert(nationality);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(nationality: Nationality): Observable<Nationality> {
        const copy = this.convert(nationality);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Nationality> {
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
     * Convert a returned JSON object to Nationality.
     */
    private convertItemFromServer(json: any): Nationality {
        const entity: Nationality = Object.assign(new Nationality(), json);
        return entity;
    }

    /**
     * Convert a Nationality to a JSON which can be sent to the server.
     */
    private convert(nationality: Nationality): Nationality {
        const copy: Nationality = Object.assign({}, nationality);
        return copy;
    }
}
