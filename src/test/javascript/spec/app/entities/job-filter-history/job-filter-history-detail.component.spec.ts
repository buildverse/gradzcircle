/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobFilterHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/job-filter-history/job-filter-history-detail.component';
import { JobFilterHistoryService } from '../../../../../../main/webapp/app/entities/job-filter-history/job-filter-history.service';
import { JobFilterHistory } from '../../../../../../main/webapp/app/entities/job-filter-history/job-filter-history.model';

describe('Component Tests', () => {

    describe('JobFilterHistory Management Detail Component', () => {
        let comp: JobFilterHistoryDetailComponent;
        let fixture: ComponentFixture<JobFilterHistoryDetailComponent>;
        let service: JobFilterHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobFilterHistoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobFilterHistoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobFilterHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobFilterHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobFilterHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobFilterHistory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobFilterHistory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
