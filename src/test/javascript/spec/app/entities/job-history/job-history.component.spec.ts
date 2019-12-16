/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { JobHistoryComponent } from 'app/entities/job-history/job-history.component';
import { JobHistoryService } from 'app/entities/job-history/job-history.service';
import { JobHistory } from 'app/entities/job-history/job-history.model';

describe('Component Tests', () => {
    describe('JobHistory Management Component', () => {
        let comp: JobHistoryComponent;
        let fixture: ComponentFixture<JobHistoryComponent>;
        let service: JobHistoryService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobHistoryComponent],
                    providers: [JobHistoryService]
                })
                    .overrideTemplate(JobHistoryComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new JobHistory(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobHistories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
