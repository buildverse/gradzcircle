/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new Candidate(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidate).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
