/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateNonAcademicWorkComponent } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.component';
import { CandidateNonAcademicWorkService } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.service';
import { CandidateNonAcademicWork } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.model';

describe('Component Tests', () => {
    describe('CandidateNonAcademicWork Management Component', () => {
        let comp: CandidateNonAcademicWorkComponent;
        let fixture: ComponentFixture<CandidateNonAcademicWorkComponent>;
        let service: CandidateNonAcademicWorkService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateNonAcademicWorkComponent],
                    providers: [CandidateNonAcademicWorkService]
                })
                    .overrideTemplate(CandidateNonAcademicWorkComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateNonAcademicWorkComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateNonAcademicWorkService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateNonAcademicWork(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateNonAcademicWorks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
