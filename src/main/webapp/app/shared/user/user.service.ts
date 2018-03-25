import { Injectable } from '@angular/core';
import { Http, Response,ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Subscriber } from 'rxjs/Subscriber';
import { SERVER_API_URL } from '../../app.constants';
import { User } from './user.model';
import { ResponseWrapper } from '../model/response-wrapper.model';
import { createRequestOption } from '../model/request-util';

@Injectable()
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private imageUrl = SERVER_API_URL +'api/files';
    private deleteImageUrl = SERVER_API_URL +'api/remove';

    private imageData: any;

    constructor(private http: Http) { }

    create(user: User): Observable<ResponseWrapper> {
        return this.http.post(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    update(user: User): Observable<ResponseWrapper> {
        return this.http.put(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    find(login: string): Observable<User> {
        return this.http.get(`${this.resourceUrl}/${login}`).map((res: Response) => res.json());
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(login: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${login}`);
    }

    authorities(): Observable<string[]> {
        return this.http.get(SERVER_API_URL + 'api/users/authorities').map((res: Response) => {
            const json = res.json();
            return <string[]> json;
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    /* ADDED BELOW FOR USER IMAGE MANAGEMENT*/
    deleteImage(id: number): Observable<Response> {
        return this.http.delete(`${this.deleteImageUrl}/${id}`).map(this.extractData).catch(this.handleError);
    }

    /*getImageData(id: string): Observable<any> {
        return new Observable((observer: Subscriber<any>) => {
            let objectUrl: string = null;
            this.http
                .get(`${this.imageUrl}/${id}`, {
                    responseType: ResponseContentType.Blob
                })
                .subscribe(m => {
                    
                    console.log("m in user sevice is "+ JSON.stringify(m));
                    objectUrl = URL.createObjectURL(m.blob());
                    observer.next(objectUrl);
                });

            return () => {
                if (objectUrl) {
                    URL.revokeObjectURL(objectUrl);
                    objectUrl = null;
                }
            };
        });
    }
*/

    getImageData(id:string): Observable<Response> {
         return this.imageData = this.http.get(`${this.imageUrl}/${id}`).map(this.extractData).catch(this.handleError); 
        
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
}
