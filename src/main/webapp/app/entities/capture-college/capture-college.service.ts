import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { CaptureCollege } from './capture-college.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CaptureCollege>;

@Injectable()
export class CaptureCollegeService {
    private resourceUrl = SERVER_API_URL + 'api/capture-colleges';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/capture-colleges';

    constructor(private http: HttpClient) {}

    create(captureCollege: CaptureCollege): Observable<EntityResponseType> {
        const copy = this.convert(captureCollege);
        return this.http
            .post<CaptureCollege>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(captureCollege: CaptureCollege): Observable<EntityResponseType> {
        const copy = this.convert(captureCollege);
        return this.http
            .put<CaptureCollege>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CaptureCollege>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<CaptureCollege[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CaptureCollege[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CaptureCollege[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CaptureCollege[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CaptureCollege[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CaptureCollege[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CaptureCollege = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<CaptureCollege[]>): HttpResponse<CaptureCollege[]> {
        const jsonResponse: CaptureCollege[] = res.body;
        const body: CaptureCollege[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CaptureCollege.
     */
    private convertItemFromServer(captureCollege: CaptureCollege): CaptureCollege {
        const copy: CaptureCollege = Object.assign({}, captureCollege);
        return copy;
    }

    /**
     * Convert a CaptureCollege to a JSON which can be sent to the server.
     */
    private convert(captureCollege: CaptureCollege): CaptureCollege {
        const copy: CaptureCollege = Object.assign({}, captureCollege);
        return copy;
    }
}
