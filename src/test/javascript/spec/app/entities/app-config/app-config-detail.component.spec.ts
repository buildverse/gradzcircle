/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AppConfigDetailComponent } from '../../../../../../main/webapp/app/entities/app-config/app-config-detail.component';
import { AppConfigService } from '../../../../../../main/webapp/app/entities/app-config/app-config.service';
import { AppConfig } from '../../../../../../main/webapp/app/entities/app-config/app-config.model';

describe('Component Tests', () => {

    describe('AppConfig Management Detail Component', () => {
        let comp: AppConfigDetailComponent;
        let fixture: ComponentFixture<AppConfigDetailComponent>;
        let service: AppConfigService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [AppConfigDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AppConfigService,
                    JhiEventManager
                ]
            }).overrideTemplate(AppConfigDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AppConfigDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppConfigService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AppConfig(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.appConfig).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
