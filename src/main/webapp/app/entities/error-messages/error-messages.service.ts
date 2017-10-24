import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { ErrorMessages } from './error-messages.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ErrorMessagesService {

    private resourceUrl = SERVER_API_URL + 'api/error-messages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/error-messages';

    constructor(private http: Http) { }

    create(errorMessages: ErrorMessages): Observable<ErrorMessages> {
        const copy = this.convert(errorMessages);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(errorMessages: ErrorMessages): Observable<ErrorMessages> {
        const copy = this.convert(errorMessages);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ErrorMessages> {
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
     * Convert a returned JSON object to ErrorMessages.
     */
    private convertItemFromServer(json: any): ErrorMessages {
        const entity: ErrorMessages = Object.assign(new ErrorMessages(), json);
        return entity;
    }

    /**
     * Convert a ErrorMessages to a JSON which can be sent to the server.
     */
    private convert(errorMessages: ErrorMessages): ErrorMessages {
        const copy: ErrorMessages = Object.assign({}, errorMessages);
        return copy;
    }
}
