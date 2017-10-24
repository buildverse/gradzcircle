/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CandidateLanguageProficiencyDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-language-proficiency/candidate-language-proficiency-detail.component';
import { CandidateLanguageProficiencyService } from '../../../../../../main/webapp/app/entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { CandidateLanguageProficiency } from '../../../../../../main/webapp/app/entities/candidate-language-proficiency/candidate-language-proficiency.model';

describe('Component Tests', () => {

    describe('CandidateLanguageProficiency Management Detail Component', () => {
        let comp: CandidateLanguageProficiencyDetailComponent;
        let fixture: ComponentFixture<CandidateLanguageProficiencyDetailComponent>;
        let service: CandidateLanguageProficiencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateLanguageProficiencyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateLanguageProficiencyService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateLanguageProficiencyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateLanguageProficiencyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateLanguageProficiencyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CandidateLanguageProficiency(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidateLanguageProficiency).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
