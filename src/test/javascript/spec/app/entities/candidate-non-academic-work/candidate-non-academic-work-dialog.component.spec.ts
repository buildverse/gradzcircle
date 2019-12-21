/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateNonAcademicWorkDialogComponent } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work-dialog.component';
import { CandidateNonAcademicWorkService } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.service';
import { CandidateNonAcademicWork } from 'app/entities/candidate-non-academic-work/candidate-non-academic-work.model';
import { CandidateService } from 'app/entities/candidate';
import { NgbModalRef, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

describe('Component Tests', () => {
    describe('CandidateNonAcademicWork Management Dialog Component', () => {
        let comp: CandidateNonAcademicWorkDialogComponent;
        let fixture: ComponentFixture<CandidateNonAcademicWorkDialogComponent>;
        let service: CandidateNonAcademicWorkService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateNonAcademicWorkDialogComponent],
                    providers: [CandidateService, CandidateNonAcademicWorkService, NgbDatepickerConfig]
                })
                    .overrideTemplate(CandidateNonAcademicWorkDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateNonAcademicWorkDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateNonAcademicWorkService);
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
                        const entity = new CandidateNonAcademicWork(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateNonAcademicWork = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateNonAcademicWorkListModification',
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
                        const entity = new CandidateNonAcademicWork();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateNonAcademicWork = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateNonAcademicWorkListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
