import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { CaptureUniversity } from './capture-university.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CaptureUniversityService {

    private resourceUrl = SERVER_API_URL + 'api/capture-universities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/capture-universities';

    constructor(private http: Http) { }

    create(captureUniversity: CaptureUniversity): Observable<CaptureUniversity> {
        const copy = this.convert(captureUniversity);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(captureUniversity: CaptureUniversity): Observable<CaptureUniversity> {
        const copy = this.convert(captureUniversity);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CaptureUniversity> {
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
     * Convert a returned JSON object to CaptureUniversity.
     */
    private convertItemFromServer(json: any): CaptureUniversity {
        const entity: CaptureUniversity = Object.assign(new CaptureUniversity(), json);
        return entity;
    }

    /**
     * Convert a CaptureUniversity to a JSON which can be sent to the server.
     */
    private convert(captureUniversity: CaptureUniversity): CaptureUniversity {
        const copy: CaptureUniversity = Object.assign({}, captureUniversity);
        return copy;
    }
}
