import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { VisaType } from './visa-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VisaTypeService {

    private resourceUrl = SERVER_API_URL + 'api/visa-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/visa-types';

    constructor(private http: Http) { }

    create(visaType: VisaType): Observable<VisaType> {
        const copy = this.convert(visaType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(visaType: VisaType): Observable<VisaType> {
        const copy = this.convert(visaType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<VisaType> {
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
     * Convert a returned JSON object to VisaType.
     */
    private convertItemFromServer(json: any): VisaType {
        const entity: VisaType = Object.assign(new VisaType(), json);
        return entity;
    }

    /**
     * Convert a VisaType to a JSON which can be sent to the server.
     */
    private convert(visaType: VisaType): VisaType {
        const copy: VisaType = Object.assign({}, visaType);
        return copy;
    }
}
