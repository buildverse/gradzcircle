import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ProfileCategory } from './profile-category.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ProfileCategory>;

@Injectable()
export class ProfileCategoryService {

    private resourceUrl =  SERVER_API_URL + 'api/profile-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/profile-categories';

    constructor(private http: HttpClient) { }

    create(profileCategory: ProfileCategory): Observable<EntityResponseType> {
        const copy = this.convert(profileCategory);
        return this.http.post<ProfileCategory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(profileCategory: ProfileCategory): Observable<EntityResponseType> {
        const copy = this.convert(profileCategory);
        return this.http.put<ProfileCategory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProfileCategory>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ProfileCategory[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProfileCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProfileCategory[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ProfileCategory[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProfileCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProfileCategory[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProfileCategory = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProfileCategory[]>): HttpResponse<ProfileCategory[]> {
        const jsonResponse: ProfileCategory[] = res.body;
        const body: ProfileCategory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ProfileCategory.
     */
    private convertItemFromServer(profileCategory: ProfileCategory): ProfileCategory {
        const copy: ProfileCategory = Object.assign({}, profileCategory);
        return copy;
    }

    /**
     * Convert a ProfileCategory to a JSON which can be sent to the server.
     */
    private convert(profileCategory: ProfileCategory): ProfileCategory {
        const copy: ProfileCategory = Object.assign({}, profileCategory);
        return copy;
    }
}
