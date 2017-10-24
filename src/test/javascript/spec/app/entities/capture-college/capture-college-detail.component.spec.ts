/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CaptureCollegeDetailComponent } from '../../../../../../main/webapp/app/entities/capture-college/capture-college-detail.component';
import { CaptureCollegeService } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.service';
import { CaptureCollege } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.model';

describe('Component Tests', () => {

    describe('CaptureCollege Management Detail Component', () => {
        let comp: CaptureCollegeDetailComponent;
        let fixture: ComponentFixture<CaptureCollegeDetailComponent>;
        let service: CaptureCollegeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureCollegeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CaptureCollegeService,
                    JhiEventManager
                ]
            }).overrideTemplate(CaptureCollegeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureCollegeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureCollegeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CaptureCollege(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.captureCollege).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
