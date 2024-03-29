/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { QualificationDialogComponent } from 'app/entities/qualification/qualification-dialog.component';
import { QualificationService } from 'app/entities/qualification/qualification.service';
import { Qualification } from 'app/entities/qualification/qualification.model';

describe('Component Tests', () => {
    describe('Qualification Management Dialog Component', () => {
        let comp: QualificationDialogComponent;
        let fixture: ComponentFixture<QualificationDialogComponent>;
        let service: QualificationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [QualificationDialogComponent],
                    providers: [QualificationService]
                })
                    .overrideTemplate(QualificationDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(QualificationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QualificationService);
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
                        const entity = new Qualification(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.qualification = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'qualificationListModification',
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
                        const entity = new Qualification();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.qualification = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'qualificationListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
