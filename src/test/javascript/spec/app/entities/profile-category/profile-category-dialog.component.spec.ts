/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { ProfileCategoryDialogComponent } from 'app/entities/profile-category/profile-category-dialog.component';
import { ProfileCategoryService } from 'app/entities/profile-category/profile-category.service';
import { ProfileCategory } from 'app/entities/profile-category/profile-category.model';
import { CandidateService } from 'app/entities/candidate';

describe('Component Tests', () => {
    describe('ProfileCategory Management Dialog Component', () => {
        let comp: ProfileCategoryDialogComponent;
        let fixture: ComponentFixture<ProfileCategoryDialogComponent>;
        let service: ProfileCategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [ProfileCategoryDialogComponent],
                    providers: [CandidateService, ProfileCategoryService]
                })
                    .overrideTemplate(ProfileCategoryDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfileCategoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfileCategoryService);
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
                        const entity = new ProfileCategory(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.profileCategory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'profileCategoryListModification',
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
                        const entity = new ProfileCategory();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.profileCategory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'profileCategoryListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
