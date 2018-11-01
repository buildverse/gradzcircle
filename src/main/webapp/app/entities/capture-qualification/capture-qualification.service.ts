import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CaptureQualification } from './capture-qualification.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CaptureQualification>;

@Injectable()
export class CaptureQualificationService {

    private resourceUrl =  SERVER_API_URL + 'api/capture-qualifications';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/capture-qualifications';

    constructor(private http: HttpClient) { }

    create(captureQualification: CaptureQualification): Observable<EntityResponseType> {
        const copy = this.convert(captureQualification);
        return this.http.post<CaptureQualification>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(captureQualification: CaptureQualification): Observable<EntityResponseType> {
        const copy = this.convert(captureQualification);
        return this.http.put<CaptureQualification>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CaptureQualification>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CaptureQualification[]>> {
        const options = createRequestOption(req);
        return this.http.get<CaptureQualification[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CaptureQualification[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CaptureQualification[]>> {
        const options = createRequestOption(req);
        return this.http.get<CaptureQualification[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CaptureQualification[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CaptureQualification = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CaptureQualification[]>): HttpResponse<CaptureQualification[]> {
        const jsonResponse: CaptureQualification[] = res.body;
        const body: CaptureQualification[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CaptureQualification.
     */
    private convertItemFromServer(captureQualification: CaptureQualification): CaptureQualification {
        const copy: CaptureQualification = Object.assign({}, captureQualification);
        return copy;
    }

    /**
     * Convert a CaptureQualification to a JSON which can be sent to the server.
     */
    private convert(captureQualification: CaptureQualification): CaptureQualification {
        const copy: CaptureQualification = Object.assign({}, captureQualification);
        return copy;
    }
}
