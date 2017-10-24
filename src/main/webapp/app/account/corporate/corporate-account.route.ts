import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { EmployerAccountComponent } from './corporate-account.component';
import { CorporateRegisterComponent } from './corporate-register.component';
import { CorporateRegisterResolver } from './corporate-register-resolver.service';
import { CorporateRegisterErrorMessagesResolver } from './corporate-register-error-message.resolver.service';

export const corporateAccountManageRoutes: Routes = [
  
  {
      path: 'employer',
      component: EmployerAccountComponent,
      data: {
        authorities: [],
        pageTitle: 'register.title'
      },
      canActivate: [UserRouteAccessService],

      children: [
        
            {
            path: 'register',
            component: CorporateRegisterComponent,
            data: {
              authorities: [],
              pageTitle: 'register.title'
            },
            canActivate: [UserRouteAccessService],
            resolve: {
              countries : CorporateRegisterResolver
            //  validationMessages : CorporateRegisterErrorMessagesResolver
            }
          },
          {
            path: 'loginCorproate',
            component: CorporateRegisterComponent,
          }
       ]
  }
];
