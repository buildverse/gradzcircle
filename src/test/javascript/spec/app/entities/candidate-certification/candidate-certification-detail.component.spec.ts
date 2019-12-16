/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateCertificationDetailComponent } from 'app/entities/candidate-certification/candidate-certification-detail.component';
import { CandidateCertificationService } from 'app/entities/candidate-certification/candidate-certification.service';
import { CandidateCertification } from 'app/entities/candidate-certification/candidate-certification.model';

describe('Component Tests', () => {
    describe('CandidateCertification Management Detail Component', () => {
        let comp: CandidateCertificationDetailComponent;
        let fixture: ComponentFixture<CandidateCertificationDetailComponent>;
        let service: CandidateCertificationService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateCertificationDetailComponent],
                    providers: [CandidateCertificationService]
                })
                    .overrideTemplate(CandidateCertificationDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateCertificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateCertificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new CandidateCertification(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateCertification).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
