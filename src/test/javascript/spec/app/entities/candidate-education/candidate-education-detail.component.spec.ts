/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateEducationService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateEducationDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new CandidateEducation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidateEducation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
