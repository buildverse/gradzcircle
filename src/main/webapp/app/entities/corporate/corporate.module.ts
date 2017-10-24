import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { GradzcircleSharedModule } from '../../shared';
import { GradzcircleAdminModule } from '../../admin/admin.module';
import { FroalaEditorModule } from 'angular-froala-wysiwyg';
import { FileUploadModule } from 'ng2-file-upload'
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
} from './';

const ENTITY_STATES = [
    ...corporateRoute,
    ...corporatePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleAdminModule,
        MultiselectDropdownModule,
        FileUploadModule,
        FroalaEditorModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CorporateComponent,
        CorporateDetailComponent,
        CorporateDialogComponent,
        CorporateDeleteDialogComponent,
        CorporatePopupComponent,
        CorporateDeletePopupComponent,
    ],
    entryComponents: [
        CorporateComponent,
        CorporateDialogComponent,
        CorporatePopupComponent,
        CorporateDeleteDialogComponent,
        CorporateDeletePopupComponent,
    ],
    providers: [
        CorporateService,
        CorporatePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCorporateModule {}
