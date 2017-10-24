/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CandidateCertificationDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-certification/candidate-certification-detail.component';
import { CandidateCertificationService } from '../../../../../../main/webapp/app/entities/candidate-certification/candidate-certification.service';
import { CandidateCertification } from '../../../../../../main/webapp/app/entities/candidate-certification/candidate-certification.model';

describe('Component Tests', () => {

    describe('CandidateCertification Management Detail Component', () => {
        let comp: CandidateCertificationDetailComponent;
        let fixture: ComponentFixture<CandidateCertificationDetailComponent>;
        let service: CandidateCertificationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateCertificationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateCertificationService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateCertificationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateCertificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateCertificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CandidateCertification(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidateCertification).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
