/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEducationComponent } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.component';
import { CandidateEducationService } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.service';
import { CandidateEducation } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.model';

describe('Component Tests', () => {

    describe('CandidateEducation Management Component', () => {
        let comp: CandidateEducationComponent;
        let fixture: ComponentFixture<CandidateEducationComponent>;
        let service: CandidateEducationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateEducationComponent],
                providers: [
                    CandidateEducationService
                ]
            })
            .overrideTemplate(CandidateEducationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEducationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEducationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CandidateEducation(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateEducations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
