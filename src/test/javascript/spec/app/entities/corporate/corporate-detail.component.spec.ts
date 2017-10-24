/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CorporateDetailComponent } from '../../../../../../main/webapp/app/entities/corporate/corporate-detail.component';
import { CorporateService } from '../../../../../../main/webapp/app/entities/corporate/corporate.service';
import { Corporate } from '../../../../../../main/webapp/app/entities/corporate/corporate.model';

describe('Component Tests', () => {

    describe('Corporate Management Detail Component', () => {
        let comp: CorporateDetailComponent;
        let fixture: ComponentFixture<CorporateDetailComponent>;
        let service: CorporateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CorporateDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CorporateService,
                    JhiEventManager
                ]
            }).overrideTemplate(CorporateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorporateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorporateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Corporate(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.corporate).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
