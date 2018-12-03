import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';
import { User } from './user.model';
import { createRequestOption } from '../model/request-util';

@Injectable()
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private imageUrl = SERVER_API_URL +'api/files';
    private deleteImageUrl = SERVER_API_URL +'api/remove';

    private imageData: any;

    constructor(private http: HttpClient) { }

     create(user: User): Observable<HttpResponse<User>> {
        return this.http.post<User>(this.resourceUrl, user, { observe: 'response' });
    }

   update(user: User): Observable<HttpResponse<User>> {
        return this.http.put<User>(this.resourceUrl, user, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<User>> {
        return this.http.get<User>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<User[]>> {
        const options = createRequestOption(req);
        return this.http.get<User[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }


    /* ADDED BELOW FOR USER IMAGE MANAGEMENT*/
    deleteImage(id: number): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.deleteImageUrl}/${id}`, { observe: 'response' });
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

    getImageData(id:string): Observable<HttpResponse<any>> {
         return this.imageData = this.http.get(`${this.imageUrl}/${id}`, { observe: 'response' });
        
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
