/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateNonAcademicWorkComponent } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.component';
import { CandidateNonAcademicWorkService } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.service';
import { CandidateNonAcademicWork } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';

describe('Component Tests', () => {
    describe('CandidateNonAcademicWork Management Component', () => {
        let comp: CandidateNonAcademicWorkComponent;
        let fixture: ComponentFixture<CandidateNonAcademicWorkComponent>;
        let service: CandidateNonAcademicWorkService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateNonAcademicWorkComponent],
                    providers: [
                        CandidateNonAcademicWorkService,
                        DataStorageService,
                        NgxSpinnerService,
                        LocalStorageService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(CandidateNonAcademicWorkComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateNonAcademicWorkComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateNonAcademicWorkService);
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
                            body: [new CandidateNonAcademicWork(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.candidateNonAcademicWorks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });
        });
    });
});
