/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateNonAcademicWorkDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-non-academic-work/candidate-non-academic-work-detail.component';
import { CandidateNonAcademicWorkService } from '../../../../../../main/webapp/app/entities/candidate-non-academic-work/candidate-non-academic-work.service';
import { CandidateNonAcademicWork } from '../../../../../../main/webapp/app/entities/candidate-non-academic-work/candidate-non-academic-work.model';

describe('Component Tests', () => {

    describe('CandidateNonAcademicWork Management Detail Component', () => {
        let comp: CandidateNonAcademicWorkDetailComponent;
        let fixture: ComponentFixture<CandidateNonAcademicWorkDetailComponent>;
        let service: CandidateNonAcademicWorkService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateNonAcademicWorkDetailComponent],
                providers: [
                    CandidateNonAcademicWorkService
                ]
            })
            .overrideTemplate(CandidateNonAcademicWorkDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateNonAcademicWorkDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateNonAcademicWorkService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CandidateNonAcademicWork(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateNonAcademicWork).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
