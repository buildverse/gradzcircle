/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateCertificationComponent } from 'app/entities/candidate-certification/candidate-certification.component';
import { CandidateCertificationService } from 'app/entities/candidate-certification/candidate-certification.service';
import { CandidateCertification } from 'app/entities/candidate-certification/candidate-certification.model';

describe('Component Tests', () => {
    describe('CandidateCertification Management Component', () => {
        let comp: CandidateCertificationComponent;
        let fixture: ComponentFixture<CandidateCertificationComponent>;
        let service: CandidateCertificationService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateCertificationComponent],
                    providers: [CandidateCertificationService]
                })
                    .overrideTemplate(CandidateCertificationComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateCertificationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateCertificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new CandidateCertification(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateCertifications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
