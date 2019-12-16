/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { JobCategoryComponent } from 'app/entities/job-category/job-category.component';
import { JobCategoryService } from 'app/entities/job-category/job-category.service';
import { JobCategory } from 'app/entities/job-category/job-category.model';

describe('Component Tests', () => {
    describe('JobCategory Management Component', () => {
        let comp: JobCategoryComponent;
        let fixture: ComponentFixture<JobCategoryComponent>;
        let service: JobCategoryService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobCategoryComponent],
                    providers: [JobCategoryService]
                })
                    .overrideTemplate(JobCategoryComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobCategoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new JobCategory(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobCategories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
