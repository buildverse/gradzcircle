import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { CaptureUniversity } from './capture-university.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CaptureUniversity>;

@Injectable()
export class CaptureUniversityService {
    private resourceUrl = SERVER_API_URL + 'api/capture-universities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/capture-universities';

    constructor(private http: HttpClient) {}

    create(captureUniversity: CaptureUniversity): Observable<EntityResponseType> {
        const copy = this.convert(captureUniversity);
        return this.http
            .post<CaptureUniversity>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(captureUniversity: CaptureUniversity): Observable<EntityResponseType> {
        const copy = this.convert(captureUniversity);
        return this.http
            .put<CaptureUniversity>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CaptureUniversity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<CaptureUniversity[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CaptureUniversity[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CaptureUniversity[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CaptureUniversity[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CaptureUniversity[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CaptureUniversity[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CaptureUniversity = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<CaptureUniversity[]>): HttpResponse<CaptureUniversity[]> {
        const jsonResponse: CaptureUniversity[] = res.body;
        const body: CaptureUniversity[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CaptureUniversity.
     */
    private convertItemFromServer(captureUniversity: CaptureUniversity): CaptureUniversity {
        const copy: CaptureUniversity = Object.assign({}, captureUniversity);
        return copy;
    }

    /**
     * Convert a CaptureUniversity to a JSON which can be sent to the server.
     */
    private convert(captureUniversity: CaptureUniversity): CaptureUniversity {
        const copy: CaptureUniversity = Object.assign({}, captureUniversity);
        return copy;
    }
}
