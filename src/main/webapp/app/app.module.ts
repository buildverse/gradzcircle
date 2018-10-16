import './vendor.ts';
import 'froala-editor/js/froala_editor.pkgd.min.js';

import { NgModule,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GradzcircleSharedModule, UserRouteAccessService } from './shared';
import { GradzcircleHomeModule } from './home/home.module';
import { GradzcircleAdminModule } from './admin/admin.module';
import { GradzcircleAccountModule } from './account/account.module';
import { GradzcircleEntityModule } from './entities/entity.module';
import { CandidateProfileModule } from './profiles/candidate/candidate-profile.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent,
} from './layouts';


@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        GradzcircleSharedModule,
        GradzcircleHomeModule,
        GradzcircleAdminModule, 
        GradzcircleAccountModule,
        GradzcircleEntityModule,
        CandidateProfileModule,
        BrowserAnimationsModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent

    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleAppModule {}
