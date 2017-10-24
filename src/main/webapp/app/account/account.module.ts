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
    CountryService,
    LoginService,
    CorporateRegisterResolver,
    LoginComponent,
    CorporateRegisterErrorMessagesResolver
} from './';

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(accountState, { useHash: true })
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
        LoginComponent,
        SettingsComponent,
        EmployerAccountComponent
    ],
    providers: [
        Register,
        ActivateService,
        PasswordService,
        PasswordResetInitService,
        PasswordResetFinishService,
        CorporateRegisterResolver,
        LoginService

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleAccountModule {}
