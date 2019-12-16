/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { JobHistoryDetailComponent } from 'app/entities/job-history/job-history-detail.component';
import { JobHistoryService } from 'app/entities/job-history/job-history.service';
import { JobHistory } from 'app/entities/job-history/job-history.model';

describe('Component Tests', () => {
    describe('JobHistory Management Detail Component', () => {
        let comp: JobHistoryDetailComponent;
        let fixture: ComponentFixture<JobHistoryDetailComponent>;
        let service: JobHistoryService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobHistoryDetailComponent],
                    providers: [JobHistoryService]
                })
                    .overrideTemplate(JobHistoryDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new JobHistory(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.jobHistory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
