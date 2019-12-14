import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Country } from './country.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Country>;

@Injectable()
export class CountryService {
    private resourceUrl = SERVER_API_URL + 'api/countries';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/countries';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/countryBySuggest';

    constructor(private http: HttpClient) {}

    create(country: Country): Observable<EntityResponseType> {
        const copy = this.convert(country);
        return this.http
            .post<Country>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(country: Country): Observable<EntityResponseType> {
        const copy = this.convert(country);
        return this.http
            .put<Country>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Country>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Country[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Country[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Country[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<Country[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Country[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Country[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Country = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<Country[]>): HttpResponse<Country[]> {
        const jsonResponse: Country[] = res.body;
        const body: Country[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    getEnabledCountries(): Observable<HttpResponse<Country[]>> {
        // console.log("Called getEnableCountires");
        return this.http
            .get<Country[]>(this.resourceUrl + '/enabled', { observe: 'response' })
            .map((res: HttpResponse<Country[]>) => this.convertArrayResponse(res));
    }

    searchRemote(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, { params: options, observe: 'response' });
    }
    /**
     * Convert a returned JSON object to Country.
     */
    private convertItemFromServer(country: Country): Country {
        const copy: Country = Object.assign({}, country);
        return copy;
    }

    /**
     * Convert a Country to a JSON which can be sent to the server.
     */
    private convert(country: Country): Country {
        const copy: Country = Object.assign({}, country);
        return copy;
    }
}
