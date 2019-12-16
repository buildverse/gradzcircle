/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateLanguageProficiencyDialogComponent } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency-dialog.component';
import { CandidateLanguageProficiencyService } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { CandidateLanguageProficiency } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency.model';
import { CandidateService } from 'app/entities/candidate';
import { LanguageService } from 'app/entities/language';

describe('Component Tests', () => {
    describe('CandidateLanguageProficiency Management Dialog Component', () => {
        let comp: CandidateLanguageProficiencyDialogComponent;
        let fixture: ComponentFixture<CandidateLanguageProficiencyDialogComponent>;
        let service: CandidateLanguageProficiencyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateLanguageProficiencyDialogComponent],
                    providers: [CandidateService, LanguageService, CandidateLanguageProficiencyService]
                })
                    .overrideTemplate(CandidateLanguageProficiencyDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateLanguageProficiencyDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateLanguageProficiencyService);
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
                        const entity = new CandidateLanguageProficiency(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateLanguageProficiency = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateLanguageProficiencyListModification',
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
                        const entity = new CandidateLanguageProficiency();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateLanguageProficiency = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateLanguageProficiencyListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
