import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { MatchTrackerComponent} from './match-tracker.component';
//import {MatchTrackerService} from './match-tracker.service';
import { JhiTrackerService, Principal } from '../../shared';

export const matchTrackerRoute: Route = {
    path: 'match-tracker',
    component: MatchTrackerComponent,
    data: {
        pageTitle: 'match.title'
    }
};