import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { ErrorMessages } from './error-messages.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ErrorMessages>;

@Injectable()
export class ErrorMessagesService {
    private resourceUrl = SERVER_API_URL + 'api/error-messages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/error-messages';

    constructor(private http: HttpClient) {}

    create(errorMessages: ErrorMessages): Observable<EntityResponseType> {
        const copy = this.convert(errorMessages);
        return this.http
            .post<ErrorMessages>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(errorMessages: ErrorMessages): Observable<EntityResponseType> {
        const copy = this.convert(errorMessages);
        return this.http
            .put<ErrorMessages>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ErrorMessages>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<ErrorMessages[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<ErrorMessages[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<ErrorMessages[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<ErrorMessages[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<ErrorMessages[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<ErrorMessages[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ErrorMessages = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<ErrorMessages[]>): HttpResponse<ErrorMessages[]> {
        const jsonResponse: ErrorMessages[] = res.body;
        const body: ErrorMessages[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to ErrorMessages.
     */
    private convertItemFromServer(errorMessages: ErrorMessages): ErrorMessages {
        const copy: ErrorMessages = Object.assign({}, errorMessages);
        return copy;
    }

    /**
     * Convert a ErrorMessages to a JSON which can be sent to the server.
     */
    private convert(errorMessages: ErrorMessages): ErrorMessages {
        const copy: ErrorMessages = Object.assign({}, errorMessages);
        return copy;
    }
}
