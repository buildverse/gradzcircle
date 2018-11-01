import './vendor.ts';
import 'froala-editor/js/froala_editor.pkgd.min.js';

import { NgModule,CUSTOM_ELEMENTS_SCHEMA,Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage, LocalStorageService, SessionStorageService  } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { GradzcircleSharedModule, UserRouteAccessService } from './shared';
import { GradzcircleAppRoutingModule} from './app-routing.module';
import { GradzcircleHomeModule } from './home/home.module';
import { GradzcircleAdminModule } from './admin/admin.module';
import { GradzcircleAccountModule } from './account/account.module';
import { GradzcircleEntityModule } from './entities/entity.module';
import { CandidateProfileModule } from './profiles/candidate/candidate-profile.module';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
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
        GradzcircleAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        GradzcircleSharedModule,
        GradzcircleHomeModule,
        GradzcircleAdminModule, 
        GradzcircleAccountModule,
        GradzcircleEntityModule,
        CandidateProfileModule
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
        PaginationConfig,
        UserRouteAccessService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [
                LocalStorageService,
                SessionStorageService
            ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [
                Injector
            ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [
                JhiEventManager
            ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [
                Injector
            ]
        }
    ],
    bootstrap: [ JhiMainComponent ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleAppModule {}
