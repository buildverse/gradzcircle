/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { JobTypeDetailComponent } from 'app/entities/job-type/job-type-detail.component';
import { JobTypeService } from 'app/entities/job-type/job-type.service';
import { JobType } from 'app/entities/job-type/job-type.model';

describe('Component Tests', () => {
    describe('JobType Management Detail Component', () => {
        let comp: JobTypeDetailComponent;
        let fixture: ComponentFixture<JobTypeDetailComponent>;
        let service: JobTypeService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobTypeDetailComponent],
                    providers: [JobTypeService]
                })
                    .overrideTemplate(JobTypeDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new JobType(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.jobType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
