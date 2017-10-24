import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Corporate } from './corporate.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CorporateService {

    private resourceUrl = SERVER_API_URL + 'api/corporates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/corporates';
    private findByLoginIdresourceUrl = SERVER_API_URL + 'api/corporateByLoginId';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(corporate: Corporate): Observable<Corporate> {
        const copy = this.convert(corporate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(corporate: Corporate): Observable<Corporate> {
        const copy = this.convert(corporate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Corporate> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

	findCorporateByLoginId(id: number): Observable<Corporate> {
        return this.http.get(`${this.findByLoginIdresourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
     * Convert a returned JSON object to Corporate.
     */
    private convertItemFromServer(json: any): Corporate {
        const entity: Corporate = Object.assign(new Corporate(), json);
        entity.establishedSince = this.dateUtils
            .convertLocalDateFromServer(json.establishedSince);
        return entity;
    }

    /**
     * Convert a Corporate to a JSON which can be sent to the server.
     */
    private convert(corporate: Corporate): Corporate {
        const copy: Corporate = Object.assign({}, corporate);
        copy.establishedSince = this.dateUtils
            .convertLocalDateToServer(corporate.establishedSince);
        return copy;
    }
}
