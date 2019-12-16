import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { VisaType } from './visa-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<VisaType>;

@Injectable()
export class VisaTypeService {
    private resourceUrl = SERVER_API_URL + 'api/visa-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/visa-types';

    constructor(private http: HttpClient) {}

    create(visaType: VisaType): Observable<EntityResponseType> {
        const copy = this.convert(visaType);
        return this.http
            .post<VisaType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(visaType: VisaType): Observable<EntityResponseType> {
        const copy = this.convert(visaType);
        return this.http
            .put<VisaType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<VisaType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<VisaType[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<VisaType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<VisaType[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<VisaType[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<VisaType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<VisaType[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: VisaType = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<VisaType[]>): HttpResponse<VisaType[]> {
        const jsonResponse: VisaType[] = res.body;
        const body: VisaType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to VisaType.
     */
    private convertItemFromServer(visaType: VisaType): VisaType {
        const copy: VisaType = Object.assign({}, visaType);
        return copy;
    }

    /**
     * Convert a VisaType to a JSON which can be sent to the server.
     */
    private convert(visaType: VisaType): VisaType {
        const copy: VisaType = Object.assign({}, visaType);
        return copy;
    }
}
