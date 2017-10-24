import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { CaptureQualification } from './capture-qualification.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CaptureQualificationService {

    private resourceUrl = SERVER_API_URL + 'api/capture-qualifications';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/capture-qualifications';

    constructor(private http: Http) { }

    create(captureQualification: CaptureQualification): Observable<CaptureQualification> {
        const copy = this.convert(captureQualification);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(captureQualification: CaptureQualification): Observable<CaptureQualification> {
        const copy = this.convert(captureQualification);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CaptureQualification> {
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
     * Convert a returned JSON object to CaptureQualification.
     */
    private convertItemFromServer(json: any): CaptureQualification {
        const entity: CaptureQualification = Object.assign(new CaptureQualification(), json);
        return entity;
    }

    /**
     * Convert a CaptureQualification to a JSON which can be sent to the server.
     */
    private convert(captureQualification: CaptureQualification): CaptureQualification {
        const copy: CaptureQualification = Object.assign({}, captureQualification);
        return copy;
    }
}
