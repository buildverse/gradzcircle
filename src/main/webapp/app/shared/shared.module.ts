import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

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
        GradzcircleSharedCommonModule
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
       // UserImagePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class GradzcircleSharedModule {}
