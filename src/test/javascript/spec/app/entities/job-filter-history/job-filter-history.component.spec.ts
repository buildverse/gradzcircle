/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { JobFilterHistoryComponent } from '../../../../../../main/webapp/app/entities/job-filter-history/job-filter-history.component';
import { JobFilterHistoryService } from '../../../../../../main/webapp/app/entities/job-filter-history/job-filter-history.service';
import { JobFilterHistory } from '../../../../../../main/webapp/app/entities/job-filter-history/job-filter-history.model';

describe('Component Tests', () => {

    describe('JobFilterHistory Management Component', () => {
        let comp: JobFilterHistoryComponent;
        let fixture: ComponentFixture<JobFilterHistoryComponent>;
        let service: JobFilterHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobFilterHistoryComponent],
                providers: [
                    JobFilterHistoryService
                ]
            })
            .overrideTemplate(JobFilterHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobFilterHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobFilterHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new JobFilterHistory(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobFilterHistories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
