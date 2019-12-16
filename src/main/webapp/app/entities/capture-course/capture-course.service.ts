import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { CaptureCourse } from './capture-course.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CaptureCourse>;

@Injectable()
export class CaptureCourseService {
    private resourceUrl = SERVER_API_URL + 'api/capture-courses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/capture-courses';

    constructor(private http: HttpClient) {}

    create(captureCourse: CaptureCourse): Observable<EntityResponseType> {
        const copy = this.convert(captureCourse);
        return this.http
            .post<CaptureCourse>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(captureCourse: CaptureCourse): Observable<EntityResponseType> {
        const copy = this.convert(captureCourse);
        return this.http
            .put<CaptureCourse>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CaptureCourse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<CaptureCourse[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CaptureCourse[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CaptureCourse[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CaptureCourse[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CaptureCourse[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CaptureCourse[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CaptureCourse = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<CaptureCourse[]>): HttpResponse<CaptureCourse[]> {
        const jsonResponse: CaptureCourse[] = res.body;
        const body: CaptureCourse[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CaptureCourse.
     */
    private convertItemFromServer(captureCourse: CaptureCourse): CaptureCourse {
        const copy: CaptureCourse = Object.assign({}, captureCourse);
        return copy;
    }

    /**
     * Convert a CaptureCourse to a JSON which can be sent to the server.
     */
    private convert(captureCourse: CaptureCourse): CaptureCourse {
        const copy: CaptureCourse = Object.assign({}, captureCourse);
        return copy;
    }
}
