import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ArchwizardModule } from 'ng2-archwizard';
import { TagInputModule } from 'ngx-chips';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
//import { MatButtonModule, MatCheckboxModule, MatRadioModule, MatInputModule, MatDatepickerModule, MatSelectModule } from '@angular/material';
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
    // UserImagePipe
} from './';

@NgModule({
    imports: [
        GradzcircleSharedLibsModule,
        GradzcircleSharedCommonModule,
        ArchwizardModule,
        MultiselectDropdownModule,TagInputModule
       // MatButtonModule, MatCheckboxModule, MatRadioModule, MatInputModule, MatDatepickerModule, MatSelectModule

    ],
    declarations: [
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        // UserImagePipe

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
        // UserImagePipe
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
        TagInputModule
      //  MatButtonModule, MatCheckboxModule, MatRadioModule, MatInputModule, MatDatepickerModule, MatSelectModule
        // UserImagePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class GradzcircleSharedModule { }
