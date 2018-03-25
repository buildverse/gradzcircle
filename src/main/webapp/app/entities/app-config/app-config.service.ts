import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { AppConfig } from './app-config.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AppConfigService {

    private resourceUrl = SERVER_API_URL + 'api/app-configs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/app-configs';

    constructor(private http: Http) { }

    create(appConfig: AppConfig): Observable<AppConfig> {
        const copy = this.convert(appConfig);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(appConfig: AppConfig): Observable<AppConfig> {
        const copy = this.convert(appConfig);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<AppConfig> {
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
     * Convert a returned JSON object to AppConfig.
     */
    private convertItemFromServer(json: any): AppConfig {
        const entity: AppConfig = Object.assign(new AppConfig(), json);
        return entity;
    }

    /**
     * Convert a AppConfig to a JSON which can be sent to the server.
     */
    private convert(appConfig: AppConfig): AppConfig {
        const copy: AppConfig = Object.assign({}, appConfig);
        return copy;
    }
}
