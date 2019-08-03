import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../shared';

import {
    Register,
    ActivateService,
    PasswordService,
    PasswordResetInitService,
    PasswordResetFinishService,
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    SocialRegisterComponent,
    SocialAuthComponent,
    CorporateRegisterComponent,
    EmployerAccountComponent,
    accountState,

    CorporateRegisterResolver,

    SideMenuComponent,
    
} from './';

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(accountState)
    ],
    declarations: [
        CorporateRegisterComponent,
        SocialRegisterComponent,
        SocialAuthComponent,
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,

        SettingsComponent,
        EmployerAccountComponent,
        SideMenuComponent
    ],
    providers: [
        Register,
        ActivateService,
        PasswordService,
        PasswordResetInitService,
        PasswordResetFinishService,
        CorporateRegisterResolver,


    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleAccountModule {}
