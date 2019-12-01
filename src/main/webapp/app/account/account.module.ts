import { GradzcircleSharedModule } from '../shared/shared.module';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    accountState,
    EmployerAccountComponent,
    CorporateRegisterComponent
} from './';

@NgModule({
    imports: [GradzcircleSharedModule, RouterModule.forChild(accountState)],
    declarations: [
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent,
        EmployerAccountComponent,
        CorporateRegisterComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleAccountModule {}
