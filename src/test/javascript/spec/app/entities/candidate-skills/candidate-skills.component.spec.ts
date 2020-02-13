/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateSkillsComponent } from 'app/entities/candidate-skills/candidate-skills.component';
import { CandidateSkillsService } from 'app/entities/candidate-skills/candidate-skills.service';
import { CandidateSkills } from 'app/entities/candidate-skills/candidate-skills.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';

describe('Component Tests', () => {
    describe('CandidateSkills Management Component', () => {
        let comp: CandidateSkillsComponent;
        let fixture: ComponentFixture<CandidateSkillsComponent>;
        let service: CandidateSkillsService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateSkillsComponent],
                    providers: [
                        CandidateSkillsService,
                        DataStorageService,
                        LocalStorageService,
                        NgxSpinnerService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(CandidateSkillsComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateSkillsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateSkillsService);
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
                            body: [new CandidateSkills(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.candidateSkills[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });
        });
    });
});
