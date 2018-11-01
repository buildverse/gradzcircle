/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { JobFilterComponent } from '../../../../../../main/webapp/app/entities/job-filter/job-filter.component';
import { JobFilterService } from '../../../../../../main/webapp/app/entities/job-filter/job-filter.service';
import { JobFilter } from '../../../../../../main/webapp/app/entities/job-filter/job-filter.model';

describe('Component Tests', () => {

    describe('JobFilter Management Component', () => {
        let comp: JobFilterComponent;
        let fixture: ComponentFixture<JobFilterComponent>;
        let service: JobFilterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobFilterComponent],
                providers: [
                    JobFilterService
                ]
            })
            .overrideTemplate(JobFilterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobFilterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobFilterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new JobFilter(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobFilters[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
