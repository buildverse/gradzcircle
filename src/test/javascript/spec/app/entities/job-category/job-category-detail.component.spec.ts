/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
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
                    JobCategoryService
                ]
            })
            .overrideTemplate(JobCategoryDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new JobCategory(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.jobCategory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
