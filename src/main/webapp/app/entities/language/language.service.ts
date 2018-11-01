import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Language } from './language.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Language>;

@Injectable()
export class LanguageService {

    private resourceUrl =  SERVER_API_URL + 'api/languages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/languages';
    private resourceSearchSuggestUrl = SERVER_API_URL + 'api/_search/languagesBySuggest';
  
    constructor(private http: HttpClient) { }

    create(language: Language): Observable<EntityResponseType> {
        const copy = this.convert(language);
        return this.http.post<Language>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(language: Language): Observable<EntityResponseType> {
        const copy = this.convert(language);
        return this.http.put<Language>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Language>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Language[]>> {
        const options = createRequestOption(req);
        return this.http.get<Language[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Language[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Language[]>> {
        const options = createRequestOption(req);
        return this.http.get<Language[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Language[]>) => this.convertArrayResponse(res));
    }
  
  searchRemote(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchSuggestUrl, { params: options, observe: 'response' });
  }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Language = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Language[]>): HttpResponse<Language[]> {
        const jsonResponse: Language[] = res.body;
        const body: Language[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Language.
     */
    private convertItemFromServer(language: Language): Language {
        const copy: Language = Object.assign({}, language);
        return copy;
    }

    /**
     * Convert a Language to a JSON which can be sent to the server.
     */
    private convert(language: Language): Language {
        const copy: Language = Object.assign({}, language);
        return copy;
    }
}
