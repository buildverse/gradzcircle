/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureCourseDialogComponent } from 'app/entities/capture-course/capture-course-dialog.component';
import { CaptureCourseService } from 'app/entities/capture-course/capture-course.service';
import { CaptureCourse } from 'app/entities/capture-course/capture-course.model';
import { CandidateEducationService } from 'app/entities/candidate-education';

describe('Component Tests', () => {
    describe('CaptureCourse Management Dialog Component', () => {
        let comp: CaptureCourseDialogComponent;
        let fixture: ComponentFixture<CaptureCourseDialogComponent>;
        let service: CaptureCourseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CaptureCourseDialogComponent],
                    providers: [CandidateEducationService, CaptureCourseService]
                })
                    .overrideTemplate(CaptureCourseDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureCourseDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureCourseService);
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
                        const entity = new CaptureCourse(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.captureCourse = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'captureCourseListModification',
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
                        const entity = new CaptureCourse();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.captureCourse = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'captureCourseListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
