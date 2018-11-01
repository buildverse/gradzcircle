/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { JobTypeComponent } from '../../../../../../main/webapp/app/entities/job-type/job-type.component';
import { JobTypeService } from '../../../../../../main/webapp/app/entities/job-type/job-type.service';
import { JobType } from '../../../../../../main/webapp/app/entities/job-type/job-type.model';

describe('Component Tests', () => {

    describe('JobType Management Component', () => {
        let comp: JobTypeComponent;
        let fixture: ComponentFixture<JobTypeComponent>;
        let service: JobTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [JobTypeComponent],
                providers: [
                    JobTypeService
                ]
            })
            .overrideTemplate(JobTypeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new JobType(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
