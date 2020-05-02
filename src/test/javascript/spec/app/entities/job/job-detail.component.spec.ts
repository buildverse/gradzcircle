/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { JobDetailComponent } from 'app/entities/job/job-detail.component';
import { JobService } from 'app/entities/job/job.service';
import { Job } from 'app/shared/job-common/job.model';

describe('Component Tests', () => {
    describe('Job Management Detail Component', () => {
        let comp: JobDetailComponent;
        let fixture: ComponentFixture<JobDetailComponent>;
        let service: JobService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobDetailComponent],
                    providers: [JobService]
                })
                    .overrideTemplate(JobDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new Job(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.job).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
