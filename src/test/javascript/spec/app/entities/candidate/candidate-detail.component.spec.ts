/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateDetailComponent } from '../../../../../../main/webapp/app/entities/candidate/candidate-detail.component';
import { CandidateService } from '../../../../../../main/webapp/app/entities/candidate/candidate.service';
import { Candidate } from '../../../../../../main/webapp/app/entities/candidate/candidate.model';

describe('Component Tests', () => {

    describe('Candidate Management Detail Component', () => {
        let comp: CandidateDetailComponent;
        let fixture: ComponentFixture<CandidateDetailComponent>;
        let service: CandidateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateDetailComponent],
                providers: [
                    CandidateService
                ]
            })
            .overrideTemplate(CandidateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Candidate(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidate).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
