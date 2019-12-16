/* tslint:disable max-line-length */
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { LocalStorageService } from 'ngx-webstorage';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEducationComponent } from 'app/entities/candidate-education/candidate-education.component';
import { CandidateEducationService } from 'app/entities/candidate-education/candidate-education.service';
import { CandidateEducation } from 'app/entities/candidate-education/candidate-education.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';

describe('Component Tests', () => {
    describe('CandidateEducation Management Component', () => {
        let comp: CandidateEducationComponent;
        let fixture: ComponentFixture<CandidateEducationComponent>;
        let service: CandidateEducationService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateEducationComponent],
                    providers: [
                        CandidateEducationService,
                        DataStorageService,
                        LocalStorageService,
                        NgxSpinnerService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(CandidateEducationComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEducationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEducationService);
            storageService = fixture.debugElement.injector.get(DataStorageService);
            mockPrincipal = fixture.debugElement.injector.get(Principal);
            ngxSpinnerService = fixture.debugElement.injector.get(NgxSpinnerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init when not candidate profile', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                const account = { authorities: ['ROLE_ADMIN'] };
                mockPrincipal.setResponse(account);
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateEducation(123)],
                            headers
                        })
                    )
                );
                // THEN
                comp.ngOnInit();

                // ASSERT
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.candidateEducations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });

            it('Should call loadEducationForCandidate on init when candidate profile', () => {
                const headers = new HttpHeaders().append('link', 'link;link');
                const account = { authorities: ['ROLE_CANDIDATE'] };
                comp.candidateEducationsForDisplay = [];
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateEducation(123)],
                            headers
                        })
                    )
                );
                mockPrincipal.setIdentitySpy(account);
                spyOn(service, 'findEducationByCandidateId').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateEducation(123)],
                            headers
                        })
                    )
                );

                spyOn(ngxSpinnerService, 'show').and.returnValue(null);
                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(ngxSpinnerService.show).toHaveBeenCalled();
                    expect(service.findEducationByCandidateId).toHaveBeenCalled();
                    expect(service.query).not.toBeCalled();
                    expect(comp.candidateEducations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });

            it('Should set highest education as primary education on from loaded candidate educations', () => {
                comp.candidateEducations = [];
                fixture.detectChanges();
                let education1: CandidateEducation;
                let education2: CandidateEducation;
                education1 = new CandidateEducation(123, 5, new Date(), null, true, 6, true);
                education1.highestQualification = true;
                education2 = new CandidateEducation(456, 5, new Date(), null, true, 6, false);
                comp.candidateEducations = [education1, education2];
                // WHEN
                comp.setPrimaryEducationOnLoad();

                // THEN
                expect(comp.primaryCandidateEducation).toEqual(jasmine.objectContaining({ id: 123 }));
                expect(comp.candidateEducationsForDisplay[0]).toEqual(jasmine.objectContaining({ id: 456 }));
            });
        });
    });
});
