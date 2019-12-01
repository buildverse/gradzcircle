import { SERVER_API_URL } from '../../app.constants';
import { createRequestOption } from '../../shared/util/request-util';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { IUser } from './user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private imageUrl = SERVER_API_URL + 'api/files';
    private deleteImageUrl = SERVER_API_URL + 'api/remove';
    private imageData: any;
    constructor(private http: HttpClient) {}

    create(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.post<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    update(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<IUser>> {
        return this.http.get<IUser>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }

    getImageData(id: string): Observable<HttpResponse<any>> {
        return (this.imageData = this.http.get(`${this.imageUrl}/${id}`, { observe: 'response' }));
    }

    private handleError(error: Response) {
        let msg = `Status code ${error.status} on url ${error.url}`;
        return Observable.throw(msg);
    }
    private extractData(response: Response) {
        let body = response;
        return body || {};
    }

    deleteImage(id: number): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.deleteImageUrl}/${id}`, { observe: 'response' });
    }
}
