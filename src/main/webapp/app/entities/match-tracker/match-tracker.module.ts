import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    // MatchTrackerService,
    MatchTrackerComponent,
    matchTrackerRoute
} from './';

@NgModule({
    imports: [GradzcircleSharedModule],
    declarations: [MatchTrackerComponent],
    entryComponents: [MatchTrackerComponent],
    providers: [
        // MatchTrackerService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleTrackerModule {}
