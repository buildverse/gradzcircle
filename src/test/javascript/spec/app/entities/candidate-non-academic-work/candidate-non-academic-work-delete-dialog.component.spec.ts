/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateNonAcademicWorkDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/candidate-non-academic-work/candidate-non-academic-work-delete-dialog.component';
import { CandidateNonAcademicWorkService } from '../../../../../../main/webapp/app/entities/candidate-non-academic-work/candidate-non-academic-work.service';

describe('Component Tests', () => {

    describe('CandidateNonAcademicWork Management Delete Component', () => {
        let comp: CandidateNonAcademicWorkDeleteDialogComponent;
        let fixture: ComponentFixture<CandidateNonAcademicWorkDeleteDialogComponent>;
        let service: CandidateNonAcademicWorkService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateNonAcademicWorkDeleteDialogComponent],
                providers: [
                    CandidateNonAcademicWorkService
                ]
            })
            .overrideTemplate(CandidateNonAcademicWorkDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateNonAcademicWorkDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateNonAcademicWorkService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
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
