import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { TagInputModule } from 'ngx-chips';
import { GradzcircleSharedLibsModule, GradzcircleSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { FutureDateValidatorDirective } from './helper/future-date-validator.directive';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { CKEditorModule } from 'ngx-ckeditor';
import { DatePipe } from '@angular/common';
import { ArchwizardModule } from 'angular-archwizard';

@NgModule({
    imports: [
        GradzcircleSharedLibsModule,
        GradzcircleSharedCommonModule,
        ArchwizardModule,
        MultiselectDropdownModule,
        TagInputModule,
        CKEditorModule
    ],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, FutureDateValidatorDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        GradzcircleSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        TagInputModule,
        CKEditorModule,
        FutureDateValidatorDirective,
        ArchwizardModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleSharedModule {}
