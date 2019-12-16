/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEmploymentDialogComponent } from 'app/entities/candidate-employment/candidate-employment-dialog.component';
import { CandidateEmploymentService } from 'app/entities/candidate-employment/candidate-employment.service';
import { CandidateEmployment } from 'app/entities/candidate-employment/candidate-employment.model';
import { CandidateService } from 'app/entities/candidate';
import { EmploymentTypeService } from 'app/entities/employment-type';
import { CountryService } from 'app/entities/country';
import { JobTypeService } from 'app/entities/job-type';

describe('Component Tests', () => {
    describe('CandidateEmployment Management Dialog Component', () => {
        let comp: CandidateEmploymentDialogComponent;
        let fixture: ComponentFixture<CandidateEmploymentDialogComponent>;
        let service: CandidateEmploymentService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateEmploymentDialogComponent],
                    providers: [CandidateService, EmploymentTypeService, CountryService, JobTypeService, CandidateEmploymentService]
                })
                    .overrideTemplate(CandidateEmploymentDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEmploymentDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEmploymentService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateEmployment(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateEmployment = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateEmploymentListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it(
                'Should call create service on save for new entity',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateEmployment();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateEmployment = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateEmploymentListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
