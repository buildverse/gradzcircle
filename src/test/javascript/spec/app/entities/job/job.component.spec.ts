/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { JobComponent } from 'app/entities/job/job.component';
import { JobService } from 'app/entities/job/job.service';
import { Job } from 'app/entities/job/job.model';

describe('Component Tests', () => {
    describe('Job Management Component', () => {
        let comp: JobComponent;
        let fixture: ComponentFixture<JobComponent>;
        let service: JobService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobComponent],
                    providers: [JobService]
                })
                    .overrideTemplate(JobComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Job(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
