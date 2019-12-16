import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { map } from 'rxjs/operators';
import { AppConfig } from './app-config.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<AppConfig>;

@Injectable()
export class AppConfigService {
    private resourceUrl = SERVER_API_URL + 'api/app-configs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/app-configs';

    constructor(private http: HttpClient) {}

    create(appConfig: AppConfig): Observable<EntityResponseType> {
        const copy = this.convert(appConfig);
        return this.http
            .post<AppConfig>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(appConfig: AppConfig): Observable<EntityResponseType> {
        const copy = this.convert(appConfig);
        return this.http
            .put<AppConfig>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<AppConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<AppConfig[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<AppConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<AppConfig[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<AppConfig[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<AppConfig[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<AppConfig[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: AppConfig = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<AppConfig[]>): HttpResponse<AppConfig[]> {
        const jsonResponse: AppConfig[] = res.body;
        const body: AppConfig[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to AppConfig.
     */
    private convertItemFromServer(appConfig: AppConfig): AppConfig {
        const copy: AppConfig = Object.assign({}, appConfig);
        return copy;
    }

    /**
     * Convert a AppConfig to a JSON which can be sent to the server.
     */
    private convert(appConfig: AppConfig): AppConfig {
        const copy: AppConfig = Object.assign({}, appConfig);
        return copy;
    }
}
