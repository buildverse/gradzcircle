/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';
import { CandidateService } from 'app/entities/candidate';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEducationDeleteDialogComponent } from 'app/entities/candidate-education/candidate-education-delete-dialog.component';
import { CandidateEducationService } from 'app/entities/candidate-education/candidate-education.service';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';

describe('Component Tests', () => {
    describe('CandidateEducation Management Delete Component', () => {
        let comp: CandidateEducationDeleteDialogComponent;
        let fixture: ComponentFixture<CandidateEducationDeleteDialogComponent>;
        let service: CandidateEducationService;
        let candidateService: CandidateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateEducationDeleteDialogComponent],
                    providers: [CandidateEducationService, CandidateService, LocalStorageService, DataStorageService, NgxSpinnerService]
                })
                    .overrideTemplate(CandidateEducationDeleteDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEducationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEducationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
            candidateService = fixture.debugElement.injector.get(CandidateService);
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
