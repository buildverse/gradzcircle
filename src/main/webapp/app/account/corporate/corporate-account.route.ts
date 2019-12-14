import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { EmployerAccountComponent } from './corporate-account.component';
import { CorporateRegisterComponent } from './corporate-register.component';
import { CorporateRegisterResolver } from './corporate-register-resolver.service';

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
                    countries: CorporateRegisterResolver
                }
            },
            {
                path: 'loginCorproate',
                component: CorporateRegisterComponent
            }
        ]
    }
];
