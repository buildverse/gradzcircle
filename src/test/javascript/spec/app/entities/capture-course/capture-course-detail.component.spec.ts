/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
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
                    CaptureCourseService
                ]
            })
            .overrideTemplate(CaptureCourseDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CaptureCourse(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.captureCourse).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
