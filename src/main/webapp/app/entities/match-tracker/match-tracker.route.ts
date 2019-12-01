import { Route } from '@angular/router';

import { MatchTrackerComponent } from './match-tracker.component';

export const matchTrackerRoute: Route = {
    path: 'match-tracker',
    component: MatchTrackerComponent,
    data: {
        pageTitle: 'match.title'
    }
};
