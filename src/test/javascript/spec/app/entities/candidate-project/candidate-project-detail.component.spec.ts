/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CandidateProjectDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project-detail.component';
import { CandidateProjectService } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.service';
import { CandidateProject } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.model';

describe('Component Tests', () => {

    describe('CandidateProject Management Detail Component', () => {
        let comp: CandidateProjectDetailComponent;
        let fixture: ComponentFixture<CandidateProjectDetailComponent>;
        let service: CandidateProjectService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateProjectDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CandidateProjectService,
                    JhiEventManager
                ]
            }).overrideTemplate(CandidateProjectDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateProjectDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateProjectService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CandidateProject(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.candidateProject).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
