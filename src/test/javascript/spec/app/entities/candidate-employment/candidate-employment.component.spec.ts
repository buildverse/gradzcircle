/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEmploymentComponent } from 'app/entities/candidate-employment/candidate-employment.component';
import { CandidateEmploymentService } from 'app/entities/candidate-employment/candidate-employment.service';
import { CandidateEmployment } from 'app/entities/candidate-employment/candidate-employment.model';

describe('Component Tests', () => {
    describe('CandidateEmployment Management Component', () => {
        let comp: CandidateEmploymentComponent;
        let fixture: ComponentFixture<CandidateEmploymentComponent>;
        let service: CandidateEmploymentService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateEmploymentComponent],
                    providers: [CandidateEmploymentService]
                })
                    .overrideTemplate(CandidateEmploymentComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEmploymentComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEmploymentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateEmployment(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateEmployments[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
