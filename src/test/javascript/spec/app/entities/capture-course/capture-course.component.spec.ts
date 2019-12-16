/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureCourseComponent } from 'app/entities/capture-course/capture-course.component';
import { CaptureCourseService } from 'app/entities/capture-course/capture-course.service';
import { CaptureCourse } from 'app/entities/capture-course/capture-course.model';

describe('Component Tests', () => {
    describe('CaptureCourse Management Component', () => {
        let comp: CaptureCourseComponent;
        let fixture: ComponentFixture<CaptureCourseComponent>;
        let service: CaptureCourseService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CaptureCourseComponent],
                    providers: [CaptureCourseService]
                })
                    .overrideTemplate(CaptureCourseComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureCourseComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureCourseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CaptureCourse(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.captureCourses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
