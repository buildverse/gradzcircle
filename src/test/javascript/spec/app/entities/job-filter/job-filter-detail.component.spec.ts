/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobFilterDetailComponent } from '../../../../../../main/webapp/app/entities/job-filter/job-filter-detail.component';
import { JobFilterService } from '../../../../../../main/webapp/app/entities/job-filter/job-filter.service';
import { JobFilter } from '../../../../../../main/webapp/app/entities/job-filter/job-filter.model';

describe('Component Tests', () => {

    describe('JobFilter Management Detail Component', () => {
        let comp: JobFilterDetailComponent;
        let fixture: ComponentFixture<JobFilterDetailComponent>;
        let service: JobFilterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobFilterDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobFilterService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobFilterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobFilterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobFilterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobFilter(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobFilter).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
