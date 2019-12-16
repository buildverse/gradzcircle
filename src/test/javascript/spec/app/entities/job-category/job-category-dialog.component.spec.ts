/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { JobCategoryDialogComponent } from 'app/entities/job-category/job-category-dialog.component';
import { JobCategoryService } from 'app/entities/job-category/job-category.service';
import { JobCategory } from 'app/entities/job-category/job-category.model';
import { CandidateService } from 'app/entities/candidate';

describe('Component Tests', () => {
    describe('JobCategory Management Dialog Component', () => {
        let comp: JobCategoryDialogComponent;
        let fixture: ComponentFixture<JobCategoryDialogComponent>;
        let service: JobCategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [JobCategoryDialogComponent],
                    providers: [CandidateService, JobCategoryService]
                })
                    .overrideTemplate(JobCategoryDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(JobCategoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobCategoryService);
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
                        const entity = new JobCategory(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.jobCategory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'jobCategoryListModification', content: 'OK' });
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
                        const entity = new JobCategory();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.jobCategory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'jobCategoryListModification', content: 'OK' });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
