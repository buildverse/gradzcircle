/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateCertificationComponent } from 'app/entities/candidate-certification/candidate-certification.component';
import { CandidateCertificationService } from 'app/entities/candidate-certification/candidate-certification.service';
import { CandidateCertification } from 'app/entities/candidate-certification/candidate-certification.model';

describe('Component Tests', () => {
    describe('CandidateCertification Management Component', () => {
        let comp: CandidateCertificationComponent;
        let fixture: ComponentFixture<CandidateCertificationComponent>;
        let service: CandidateCertificationService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateCertificationComponent],
                    providers: [
                        CandidateCertificationService,
                        DataStorageService,
                        LocalStorageService,
                        NgxSpinnerService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(CandidateCertificationComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateCertificationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateCertificationService);
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
                            body: [new CandidateCertification(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.candidateCertifications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });
        });
    });
});
