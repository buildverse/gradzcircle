/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEmploymentComponent } from 'app/entities/candidate-employment/candidate-employment.component';
import { CandidateEmploymentService } from 'app/entities/candidate-employment/candidate-employment.service';
import { CandidateEmployment } from 'app/entities/candidate-employment/candidate-employment.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';

describe('Component Tests', () => {
    describe('CandidateEmployment Management Component', () => {
        let comp: CandidateEmploymentComponent;
        let fixture: ComponentFixture<CandidateEmploymentComponent>;
        let service: CandidateEmploymentService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateEmploymentComponent],
                    providers: [
                        CandidateEmploymentService,
                        DataStorageService,
                        LocalStorageService,
                        NgxSpinnerService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(CandidateEmploymentComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEmploymentComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEmploymentService);
            storageService = fixture.debugElement.injector.get(DataStorageService);
            mockPrincipal = fixture.debugElement.injector.get(Principal);
            ngxSpinnerService = fixture.debugElement.injector.get(NgxSpinnerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                const account = { authorities: ['ROLE_ADMIN'] };
                mockPrincipal.setResponse(account);
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateEmployment(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.candidateEmployments[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });
        });
    });
});
