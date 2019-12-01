import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { GradzcircleSharedModule } from '../../shared';
import { GradzcircleAdminModule } from '../../admin/admin.module';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { FileUploadModule } from 'ng2-file-upload';
import {
    CorporateService,
    CorporatePopupService,
    CorporateComponent,
    CorporateDetailComponent,
    CorporateDialogComponent,
    CorporatePopupComponent,
    CorporateDeletePopupComponent,
    CorporateDeleteDialogComponent,
    corporateRoute,
    corporatePopupRoute,
    LinkedCandidatesComponent,
    LinkedCandidatesResolvePagingParams,
    CorporateResolverService
} from './';

const ENTITY_STATES = [...corporateRoute, ...corporatePopupRoute];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleAdminModule,
        MultiselectDropdownModule,
        FileUploadModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CorporateComponent,
        CorporateDetailComponent,
        CorporateDialogComponent,
        CorporateDeleteDialogComponent,
        CorporatePopupComponent,
        CorporateDeletePopupComponent,
        LinkedCandidatesComponent
    ],
    entryComponents: [
        CorporateComponent,
        CorporateDialogComponent,
        CorporatePopupComponent,
        CorporateDeleteDialogComponent,
        CorporateDeletePopupComponent,
        LinkedCandidatesComponent
    ],
    providers: [CorporateService, CorporatePopupService, CorporateResolverService, LinkedCandidatesResolvePagingParams, DataStorageService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCorporateModule {}
