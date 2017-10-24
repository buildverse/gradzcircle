/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateEmploymentService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateEmploymentDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new CandidateEmployment(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidateEmployment).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
