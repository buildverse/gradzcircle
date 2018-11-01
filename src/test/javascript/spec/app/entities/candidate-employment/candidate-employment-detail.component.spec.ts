/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEmploymentDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-employment/candidate-employment-detail.component';
import { CandidateEmploymentService } from '../../../../../../main/webapp/app/entities/candidate-employment/candidate-employment.service';
import { CandidateEmployment } from '../../../../../../main/webapp/app/entities/candidate-employment/candidate-employment.model';

describe('Component Tests', () => {

    describe('CandidateEmployment Management Detail Component', () => {
        let comp: CandidateEmploymentDetailComponent;
        let fixture: ComponentFixture<CandidateEmploymentDetailComponent>;
        let service: CandidateEmploymentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateEmploymentDetailComponent],
                providers: [
                    CandidateEmploymentService
                ]
            })
            .overrideTemplate(CandidateEmploymentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEmploymentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEmploymentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CandidateEmployment(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateEmployment).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
