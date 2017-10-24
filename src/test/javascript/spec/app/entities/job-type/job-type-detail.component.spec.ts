/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobTypeDetailComponent } from '../../../../../../main/webapp/app/entities/job-type/job-type-detail.component';
import { JobTypeService } from '../../../../../../main/webapp/app/entities/job-type/job-type.service';
import { JobType } from '../../../../../../main/webapp/app/entities/job-type/job-type.model';

describe('Component Tests', () => {

    describe('JobType Management Detail Component', () => {
        let comp: JobTypeDetailComponent;
        let fixture: ComponentFixture<JobTypeDetailComponent>;
        let service: JobTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
