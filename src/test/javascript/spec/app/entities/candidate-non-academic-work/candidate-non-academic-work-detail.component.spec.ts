/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateNonAcademicWorkService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateNonAcademicWorkDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new CandidateNonAcademicWork(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidateNonAcademicWork).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
