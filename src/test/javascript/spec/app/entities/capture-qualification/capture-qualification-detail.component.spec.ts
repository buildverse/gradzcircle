/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CaptureQualificationDetailComponent } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification-detail.component';
import { CaptureQualificationService } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.service';
import { CaptureQualification } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.model';

describe('Component Tests', () => {

    describe('CaptureQualification Management Detail Component', () => {
        let comp: CaptureQualificationDetailComponent;
        let fixture: ComponentFixture<CaptureQualificationDetailComponent>;
        let service: CaptureQualificationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureQualificationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CaptureQualificationService,
                    JhiEventManager
                ]
            }).overrideTemplate(CaptureQualificationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureQualificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureQualificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CaptureQualification(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.captureQualification).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
