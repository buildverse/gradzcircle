/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { JobFilterHistoryDetailComponent } from 'app/entities/job-filter-history/job-filter-history-detail.component';
import { JobFilterHistoryService } from 'app/entities/job-filter-history/job-filter-history.service';
import { JobFilterHistory } from 'app/entities/job-filter-history/job-filter-history.model';

describe('Component Tests', () => {
    describe('JobFilterHistory Management Detail Component', () => {
        let comp: JobFilterHistoryDetailComponent;
        let fixture: ComponentFixture<JobFilterHistoryDetailComponent>;
        let service: JobFilterHistoryService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobFilterHistoryDetailComponent],
                    providers: [JobFilterHistoryService]
                })
                    .overrideTemplate(JobFilterHistoryDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobFilterHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobFilterHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new JobFilterHistory(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.jobFilterHistory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
