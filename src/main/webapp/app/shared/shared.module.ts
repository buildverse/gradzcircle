import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ArchwizardModule } from 'ng2-archwizard';
import { TagInputModule } from 'ngx-chips';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { CKEditorModule } from 'ngx-ckeditor';
import {
    GradzcircleSharedLibsModule,
    GradzcircleSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    JhiTrackerService,
    HasAnyAuthorityDirective,
    JhiSocialComponent,
    SocialService,
    JhiLoginModalComponent,
    DataStorageService,
    LoginEmitterService,
    FutureDateValidatorDirective

} from './';

@NgModule({
    imports: [
        GradzcircleSharedLibsModule,
        GradzcircleSharedCommonModule,
        ArchwizardModule,
        MultiselectDropdownModule,
        TagInputModule,
        CKEditorModule
    ],
    declarations: [
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        FutureDateValidatorDirective

    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        JhiTrackerService,
        AuthServerProvider,
        SocialService,
        UserService,
        DatePipe,
        DataStorageService,
        LoginEmitterService
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        GradzcircleSharedCommonModule,
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe,
        ArchwizardModule,
        MultiselectDropdownModule,
        TagInputModule,
        CKEditorModule,
        FutureDateValidatorDirective

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class GradzcircleSharedModule { }
