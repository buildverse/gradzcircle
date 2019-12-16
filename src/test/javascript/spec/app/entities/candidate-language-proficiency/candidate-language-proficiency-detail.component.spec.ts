/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateLanguageProficiencyDetailComponent } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency-detail.component';
import { CandidateLanguageProficiencyService } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { CandidateLanguageProficiency } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency.model';

describe('Component Tests', () => {
    describe('CandidateLanguageProficiency Management Detail Component', () => {
        let comp: CandidateLanguageProficiencyDetailComponent;
        let fixture: ComponentFixture<CandidateLanguageProficiencyDetailComponent>;
        let service: CandidateLanguageProficiencyService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateLanguageProficiencyDetailComponent],
                    providers: [CandidateLanguageProficiencyService]
                })
                    .overrideTemplate(CandidateLanguageProficiencyDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateLanguageProficiencyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateLanguageProficiencyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new CandidateLanguageProficiency(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateLanguageProficiency).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
