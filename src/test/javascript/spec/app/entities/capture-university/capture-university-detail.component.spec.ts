/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CaptureUniversityDetailComponent } from '../../../../../../main/webapp/app/entities/capture-university/capture-university-detail.component';
import { CaptureUniversityService } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.service';
import { CaptureUniversity } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.model';

describe('Component Tests', () => {

    describe('CaptureUniversity Management Detail Component', () => {
        let comp: CaptureUniversityDetailComponent;
        let fixture: ComponentFixture<CaptureUniversityDetailComponent>;
        let service: CaptureUniversityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureUniversityDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CaptureUniversityService,
                    JhiEventManager
                ]
            }).overrideTemplate(CaptureUniversityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureUniversityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureUniversityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CaptureUniversity(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.captureUniversity).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
