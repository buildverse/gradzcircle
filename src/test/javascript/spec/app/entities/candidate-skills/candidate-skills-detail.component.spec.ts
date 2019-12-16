/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateSkillsDetailComponent } from 'app/entities/candidate-skills/candidate-skills-detail.component';
import { CandidateSkillsService } from 'app/entities/candidate-skills/candidate-skills.service';
import { CandidateSkills } from 'app/entities/candidate-skills/candidate-skills.model';

describe('Component Tests', () => {
    describe('CandidateSkills Management Detail Component', () => {
        let comp: CandidateSkillsDetailComponent;
        let fixture: ComponentFixture<CandidateSkillsDetailComponent>;
        let service: CandidateSkillsService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateSkillsDetailComponent],
                    providers: [CandidateSkillsService]
                })
                    .overrideTemplate(CandidateSkillsDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateSkillsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateSkillsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new CandidateSkills(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateSkills).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
