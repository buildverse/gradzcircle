import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { LoginComponent } from './login.component';

export const loginRoute: Route = {
  path: 'login',
  component: LoginComponent,
  data: {
    authorities: [],
    pageTitle: 'login.title'
  },
   canActivate: [UserRouteAccessService]
};
