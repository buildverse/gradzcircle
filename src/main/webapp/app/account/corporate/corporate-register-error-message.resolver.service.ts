import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ErrorMessages } from '../../entities/error-messages/error-messages.model';
import { ErrorMessagesService } from '../../entities/error-messages/error-messages.service';

@Injectable()

export class CorporateRegisterErrorMessagesResolver implements Resolve <ErrorMessages[]> {
    constructor (private errorMessageService: ErrorMessagesService, private router: Router) {}
    componentName = 'corporateRegister';

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise <ErrorMessages[]>{
        return null;
        // return this.errorMessageService.getErrorMessages(this.componentName).toPromise()
        //     .then( errorMessages => {
        //         return errorMessages}
        //         );
    }

}
