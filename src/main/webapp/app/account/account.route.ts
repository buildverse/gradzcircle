import { Routes } from '@angular/router';

import {
    activateRoute,
    loginRoute,
    corporateAccountManageRoutes,
    passwordRoute,
    passwordResetFinishRoute,
    passwordResetInitRoute,
    registerRoute,
    socialRegisterRoute,
    socialAuthRoute,
    settingsRoute
} from './';

const ACCOUNT_ROUTES = [
   activateRoute,
   passwordRoute,
   passwordResetFinishRoute,
   passwordResetInitRoute,
   registerRoute,
   loginRoute,
    ...corporateAccountManageRoutes,
    socialAuthRoute,
    socialRegisterRoute,
    settingsRoute
];

export const accountState: Routes = [{
    path: '',
    children: ACCOUNT_ROUTES
}];
