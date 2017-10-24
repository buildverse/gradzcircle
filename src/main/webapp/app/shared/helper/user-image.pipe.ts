// import {
//     Pipe,
//     PipeTransform,
//     OnDestroy,
//     WrappedValue,
//     ChangeDetectorRef
// } from '@angular/core';

// import { Subscription } from 'rxjs/Subscription';
// import { Observable } from 'rxjs/Observable';
// import { BehaviorSubject } from 'rxjs/BehaviorSubject';
// import 'rxjs/add/observable/of';

// import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

// import { UserService } from '../../shared';

// // Using similarity from AsyncPipe to avoid having to pipe |secure|async in HTML.
// @Pipe({
//     name: 'secure',
//     pure: false
// })
// export class UserImagePipe implements PipeTransform, OnDestroy {
//     private _latestValue: any = null;
//     private _latestReturnedValue: any = null;
//     private _subscription: Subscription = null;
//     private _obj: Observable<any> = null;

//     private previousUrl: string;
//     private _result: BehaviorSubject<any> = new BehaviorSubject('');
//     private result: Observable<any> = this._result.asObservable();
//     private _internalSubscription: Subscription = null;

//     constructor(
//         private _ref: ChangeDetectorRef,
//         private userService: UserService,
//         private sanitizer: DomSanitizer
//     ) { }

//     ngOnDestroy(): void {
//         if (this._subscription) {
//             this._dispose();
//         }
//     }

//     transform(url: string): any {
//         let obj = this.internalTransform(url);
//         //console.log("Obj is "+JSON.stringify(obj));
//         return this.asyncTrasnform(obj);
//     }

//     private internalTransform(url: string): Observable<any> {
//         if (!url) {
//             return this.result;
//         }    
//         if (this.previousUrl !== url) {
//             this.previousUrl = url;
            
//             /* THIS IS A HACK NEED TO REMOVE ONCE UNDERSTAND HOW TO SEND EVET TO PARENT FOR IMG */
//             var splitUrl = url.split('-');
//             url = splitUrl[0];

//             this._internalSubscription = this.userService.getImageData(url).subscribe(m => {
                
//                 let sanitized = this.sanitizer.bypassSecurityTrustUrl(m.json().href);
//                 console.log("link is "+ JSON.stringify(sanitized));
//                 this._result.next(sanitized);
//             });
//         }
//         return this.result;
//     }

//     private asyncTrasnform(obj: Observable<any>): any {
//         if (!this._obj) {
//             if (obj) {
//                 this._subscribe(obj);
//             }
//             this._latestReturnedValue = this._latestValue;
//             return this._latestValue;
//         }
//         if (obj !== this._obj) {
//             this._dispose();
//             return this.asyncTrasnform(obj);
//         }
//         if (this._latestValue === this._latestReturnedValue) {
//             return this._latestReturnedValue;
//         }
//         this._latestReturnedValue = this._latestValue;
//         return WrappedValue.wrap(this._latestValue);
//     }

//     private _subscribe(obj: Observable<any>) {
//         var _this = this;
//         this._obj = obj;

//         this._subscription = obj.subscribe({
//             next: function (value) {
//                 return _this._updateLatestValue(obj, value);
//             }, error: (e: any) => { throw e; }
//         });
//     }

//     private _dispose() {
//         this._subscription.unsubscribe();
//         if(this._internalSubscription)
//             this._internalSubscription.unsubscribe();
//         this._internalSubscription = null;
//         this._latestValue = null;
//         this._latestReturnedValue = null;
//         this._subscription = null;
//         this._obj = null;
//     }

//     private _updateLatestValue(async: any, value: Object) {
//         if (async === this._obj) {
//             this._latestValue = value;
//             this._ref.markForCheck();
//         }
//     }
// }