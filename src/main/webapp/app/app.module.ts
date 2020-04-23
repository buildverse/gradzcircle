import './vendor.ts';

import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from '@angular/core';
import { DefaultUrlSerializer, UrlSerializer, UrlTree } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage, LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';
import { NgxSpinnerModule } from 'ngx-spinner';
import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { GradzcircleSharedModule } from 'app/shared';
import { GradzcircleCoreModule } from 'app/core';
import { GradzcircleAppRoutingModule } from './app-routing.module';
import { GradzcircleHomeModule } from './home/home.module';
import { GradzcircleAccountModule } from './account/account.module';
import { CorporateService } from './entities/corporate/corporate.service';
import { CandidateService } from './entities/candidate/candidate.service';
import { AppConfigService } from './entities/app-config/app-config.service';
import { CountryService } from './entities/country/country.service';
// import { CandidateProfileModule } from './profiles/candidate/candidate-profile.module';
// import { GradzcircleEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ActiveMenuDirective, ErrorComponent } from './layouts';

export class MyUrlSerializer extends DefaultUrlSerializer implements UrlSerializer {
    /** Converts a `UrlTree` into a url */
    serialize(tree: UrlTree): string {
        return super.serialize(tree).replace(/\((((?!(\/\/)).)*)\)/g, '$1');
    }
}

@NgModule({
    imports: [
        BrowserModule,
        GradzcircleAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        GradzcircleSharedModule,
        GradzcircleCoreModule,
        GradzcircleHomeModule,
        GradzcircleAccountModule,
        // GradzcircleCorporateModule,
        //  GradzcircleCandidateModule,
        //  GradzcircleEntityModule,
        //  CandidateProfileModule,
        //  GradzcircleAppConfigModule,
        NgxSpinnerModule,
        BrowserAnimationsModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        },
        { provide: UrlSerializer, useClass: MyUrlSerializer },
        AppConfigService,
        CountryService,
        CorporateService,
        CandidateService
    ],
    bootstrap: [JhiMainComponent]
})
export class GradzcircleAppModule {}
