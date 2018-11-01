/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
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
                    JobFilterService
                ]
            })
            .overrideTemplate(JobFilterDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new JobFilter(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.jobFilter).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
