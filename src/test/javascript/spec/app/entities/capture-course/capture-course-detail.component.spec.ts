/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CaptureCourseDetailComponent } from '../../../../../../main/webapp/app/entities/capture-course/capture-course-detail.component';
import { CaptureCourseService } from '../../../../../../main/webapp/app/entities/capture-course/capture-course.service';
import { CaptureCourse } from '../../../../../../main/webapp/app/entities/capture-course/capture-course.model';

describe('Component Tests', () => {

    describe('CaptureCourse Management Detail Component', () => {
        let comp: CaptureCourseDetailComponent;
        let fixture: ComponentFixture<CaptureCourseDetailComponent>;
        let service: CaptureCourseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureCourseDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CaptureCourseService,
                    JhiEventManager
                ]
            }).overrideTemplate(CaptureCourseDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureCourseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureCourseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CaptureCourse(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.captureCourse).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
