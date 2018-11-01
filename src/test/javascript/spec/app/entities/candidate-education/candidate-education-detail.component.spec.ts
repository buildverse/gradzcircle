/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEducationDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education-detail.component';
import { CandidateEducationService } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.service';
import { CandidateEducation } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.model';

describe('Component Tests', () => {

    describe('CandidateEducation Management Detail Component', () => {
        let comp: CandidateEducationDetailComponent;
        let fixture: ComponentFixture<CandidateEducationDetailComponent>;
        let service: CandidateEducationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateEducationDetailComponent],
                providers: [
                    CandidateEducationService
                ]
            })
            .overrideTemplate(CandidateEducationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEducationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEducationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CandidateEducation(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateEducation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
