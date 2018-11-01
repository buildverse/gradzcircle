/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateLanguageProficiencyComponent } from '../../../../../../main/webapp/app/entities/candidate-language-proficiency/candidate-language-proficiency.component';
import { CandidateLanguageProficiencyService } from '../../../../../../main/webapp/app/entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { CandidateLanguageProficiency } from '../../../../../../main/webapp/app/entities/candidate-language-proficiency/candidate-language-proficiency.model';

describe('Component Tests', () => {

    describe('CandidateLanguageProficiency Management Component', () => {
        let comp: CandidateLanguageProficiencyComponent;
        let fixture: ComponentFixture<CandidateLanguageProficiencyComponent>;
        let service: CandidateLanguageProficiencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateLanguageProficiencyComponent],
                providers: [
                    CandidateLanguageProficiencyService
                ]
            })
            .overrideTemplate(CandidateLanguageProficiencyComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateLanguageProficiencyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateLanguageProficiencyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CandidateLanguageProficiency(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateLanguageProficiencies[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
