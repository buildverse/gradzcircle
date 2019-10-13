/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateSkillsComponent } from '../../../../../../main/webapp/app/entities/candidate-skills/candidate-skills.component';
import { CandidateSkillsService } from '../../../../../../main/webapp/app/entities/candidate-skills/candidate-skills.service';
import { CandidateSkills } from '../../../../../../main/webapp/app/entities/candidate-skills/candidate-skills.model';

describe('Component Tests', () => {

    describe('CandidateSkills Management Component', () => {
        let comp: CandidateSkillsComponent;
        let fixture: ComponentFixture<CandidateSkillsComponent>;
        let service: CandidateSkillsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateSkillsComponent],
                providers: [
                    CandidateSkillsService
                ]
            })
            .overrideTemplate(CandidateSkillsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateSkillsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateSkillsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CandidateSkills(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateSkills[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
