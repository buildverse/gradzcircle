/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateLanguageProficiencyDeleteDialogComponent } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency-delete-dialog.component';
import { CandidateLanguageProficiencyService } from 'app/entities/candidate-language-proficiency/candidate-language-proficiency.service';

describe('Component Tests', () => {
    describe('CandidateLanguageProficiency Management Delete Component', () => {
        let comp: CandidateLanguageProficiencyDeleteDialogComponent;
        let fixture: ComponentFixture<CandidateLanguageProficiencyDeleteDialogComponent>;
        let service: CandidateLanguageProficiencyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateLanguageProficiencyDeleteDialogComponent],
                    providers: [CandidateLanguageProficiencyService]
                })
                    .overrideTemplate(CandidateLanguageProficiencyDeleteDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateLanguageProficiencyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateLanguageProficiencyService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
