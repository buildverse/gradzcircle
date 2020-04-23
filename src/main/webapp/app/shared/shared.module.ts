import { JhiLanguageHelper } from '../core/language/language.helper';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { TagInputModule } from 'ngx-chips';
import { GradzcircleSharedLibsModule, GradzcircleSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { FutureDateValidatorDirective } from './helper/future-date-validator.directive';
import { DataStorageService } from './helper/localstorage.service';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { CKEditorModule } from 'ngx-ckeditor';
import { DatePipe } from '@angular/common';
import { ArchwizardModule } from 'angular-archwizard';
import { JhiLanguageService } from 'ng-jhipster';

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
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }, DataStorageService],
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
export class GradzcircleSharedModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
