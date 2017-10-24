import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions,ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Subscriber } from 'rxjs/Subscriber';
import { JhiDateUtils } from 'ng-jhipster';
import { Candidate } from './candidate.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class CandidateService {

    private resourceUrl = SERVER_API_URL + 'api/candidates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidates';
    private imageUrl = 'api/files';
    private deleteImageUrl ='api/remove';
    private resourcePublicProfileUrl ='api/candidates/public-profile'

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(candidate: Candidate): Observable<Candidate> {
        const copy = this.convert(candidate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(candidate: Candidate): Observable<Candidate> {
        const copy = this.convert(candidate);
        //console.log("Sending candidte over wire" + JSON.stringify(copy));
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    getCandidateByLoginId(id: string): Observable<Candidate> {
        return this.http.get(`api/candidateByLogin/${id}`)
            .map((response: Response) => <Candidate>response.json())
            .catch(this.handleError);
    }


    find(id: number): Observable<Candidate> {
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

    deleteImage(id: number): Observable<Response> {
        return this.http.delete(`${this.deleteImageUrl}/${id}`).map(this.extractData).catch(this.handleError);
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
     * Convert a returned JSON object to Candidate.
     */
    private convertItemFromServer(json: any): Candidate {
        const entity: Candidate = Object.assign(new Candidate(), json);
        entity.dateOfBirth = this.dateUtils
            .convertLocalDateFromServer(json.dateOfBirth);
        return entity;
    }

    /**
     * Convert a Candidate to a JSON which can be sent to the server.
     */
    private convert(candidate: Candidate): Candidate {
        const copy: Candidate = Object.assign({}, candidate);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(candidate.dateOfBirth);
        return copy;
    }

    private handleError(error: Response) {

        let msg = `Status code ${error.status} on url ${error.url}`;
        return Observable.throw(msg);
    }

    // getProfilePic(id: number): Observable<any> {
    //     return this.http.get(`api/files/${id}`).map(this.extractData).catch(this.handleError);
    // }

    private extractData(response: Response) {
        let body = response;
        return body || {};
    }


    // getImageData(url: string): Observable<any> {
    //         return new Observable((observer: Subscriber<any>) => {
    //             let objectUrl: string = null;
    //             this.http
    //                 .get(url, {
    //                     responseType: ResponseContentType.Blob
    //                 })
    //                 .subscribe(m => {
    //                     objectUrl = URL.createObjectURL(m.blob());
    //                     observer.next(objectUrl);
    //                 });

    //             return () => {
    //                 if (objectUrl) {
    //                     URL.revokeObjectURL(objectUrl);
    //                     objectUrl = null;
    //                 }
    //             };
    //         });
    //     }

    getPublicProfile(req?: any): Observable<Candidate> {
        const options = createRequestOption(req);
        return this.http.get(this.resourcePublicProfileUrl,options).map((res: Response) => {
            const jsonResponse = res.json();
           // this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

}
