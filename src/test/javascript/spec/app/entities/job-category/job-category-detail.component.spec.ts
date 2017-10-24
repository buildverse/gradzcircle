/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobCategoryDetailComponent } from '../../../../../../main/webapp/app/entities/job-category/job-category-detail.component';
import { JobCategoryService } from '../../../../../../main/webapp/app/entities/job-category/job-category.service';
import { JobCategory } from '../../../../../../main/webapp/app/entities/job-category/job-category.model';

describe('Component Tests', () => {

    describe('JobCategory Management Detail Component', () => {
        let comp: JobCategoryDetailComponent;
        let fixture: ComponentFixture<JobCategoryDetailComponent>;
        let service: JobCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobCategoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobCategoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobCategoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobCategoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobCategory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobCategory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
