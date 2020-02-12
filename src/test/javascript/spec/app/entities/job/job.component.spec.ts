/* tslint:disable max-line-length */
import { AppConfigService } from 'app/entities/app-config';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { GradzcircleTestModule } from '../../../test.module';
import { JobComponent } from 'app/entities/job/job.component';
import { JobService } from 'app/entities/job/job.service';
import { Job } from 'app/entities/job/job.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';

describe('Component Tests', () => {
    describe('Job Management Component', () => {
        let comp: JobComponent;
        let fixture: ComponentFixture<JobComponent>;
        let service: JobService;
        let appConfigService: AppConfigService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobComponent],
                    providers: [
                        JobService,
                        DataStorageService,
                        LocalStorageService,
                        NgxSpinnerService,
                        AppConfigService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(JobComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobService);
            appConfigService = fixture.debugElement.injector.get(AppConfigService);
            storageService = fixture.debugElement.injector.get(DataStorageService);
            mockPrincipal = fixture.debugElement.injector.get(Principal);
            ngxSpinnerService = fixture.debugElement.injector.get(NgxSpinnerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                const account = { authorities: ['ROLE_ADMIN'] };
                //  mockPrincipal.setResponse(account);
                spyOn(storageService, 'getData').and.returnValue('ROLE_ADMIN');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Job(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.jobs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });
        });
    });
});
