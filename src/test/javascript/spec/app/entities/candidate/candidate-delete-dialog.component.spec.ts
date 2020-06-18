/* tslint:disable max-line-length */
import { MockRouter } from '../../../helpers/mock-route.service';
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { Router } from '@angular/router';
import { CandidateDeleteDialogComponent } from 'app/entities/candidate/candidate-delete-dialog.component';
import { CandidateService } from 'app/entities/candidate/candidate.service';

describe('Component Tests', () => {
    describe('Candidate Management Delete Component', () => {
        let comp: CandidateDeleteDialogComponent;
        let fixture: ComponentFixture<CandidateDeleteDialogComponent>;
        let service: CandidateService;
        let mockEventManager: any;
        let mockActiveModal: any;
        let mockRouter: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateDeleteDialogComponent],
                    providers: [CandidateService, { provide: Router, useClass: MockRouter }]
                })
                    .overrideTemplate(CandidateDeleteDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
            mockRouter = fixture.debugElement.injector.get(Router);
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
            it(
                'Should clear Router correctly when user clicks clear from html',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.clear();
                        tick();

                        // THEN
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin', { outlets: { popup: null } }]);
                    })
                )
            );
        });
    });
});
