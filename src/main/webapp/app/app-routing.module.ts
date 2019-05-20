import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from './app.constants';

const LAYOUT_ROUTES = [
    navbarRoute,
   /* {
        path: 'candidate-profile',
        loadChildren: './profiles/candidate/candidate-profile.module#CandidateProfileModule'
     },*/
    ...errorRoute
];

@NgModule({
    imports: [
        RouterModule.forRoot(LAYOUT_ROUTES, { useHash: true, onSameUrlNavigation: 'reload' })
    ],
    exports: [
        RouterModule
    ]
})
export class GradzcircleAppRoutingModule {}
