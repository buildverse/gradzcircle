import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Address } from './address.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Address>;

@Injectable()
export class AddressService {

    private resourceUrl =  SERVER_API_URL + 'api/addresses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/addresses';

    constructor(private http: HttpClient) { }

    create(address: Address): Observable<EntityResponseType> {
        const copy = this.convert(address);
        return this.http.post<Address>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(address: Address): Observable<EntityResponseType> {
        const copy = this.convert(address);
        return this.http.put<Address>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Address>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Address[]>> {
        const options = createRequestOption(req);
        return this.http.get<Address[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Address[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Address[]>> {
        const options = createRequestOption(req);
        return this.http.get<Address[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Address[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Address = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Address[]>): HttpResponse<Address[]> {
        const jsonResponse: Address[] = res.body;
        const body: Address[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Address.
     */
    private convertItemFromServer(address: Address): Address {
        const copy: Address = Object.assign({}, address);
        return copy;
    }

    /**
     * Convert a Address to a JSON which can be sent to the server.
     */
    private convert(address: Address): Address {
        const copy: Address = Object.assign({}, address);
        return copy;
    }
}
