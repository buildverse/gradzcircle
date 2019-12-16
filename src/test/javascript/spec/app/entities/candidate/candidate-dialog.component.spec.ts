/* tslint:disable max-line-length */
import { UserService } from 'app/core/user/user.service';
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateDialogComponent } from 'app/entities/candidate/candidate-dialog.component';
import { CandidateService } from 'app/entities/candidate/candidate.service';
import { Candidate } from 'app/entities/candidate/candidate.model';
import { NationalityService } from 'app/entities/nationality';
import { GenderService } from 'app/entities/gender';
import { MaritalStatusService } from 'app/entities/marital-status';
import { JobCategoryService } from 'app/entities/job-category';
import { JobService } from 'app/entities/job';
import { ProfileCategoryService } from 'app/entities/profile-category';
import { CorporateService } from 'app/entities/corporate';
import { VisaTypeService } from 'app/entities/visa-type';

describe('Component Tests', () => {
    describe('Candidate Management Dialog Component', () => {
        let comp: CandidateDialogComponent;
        let fixture: ComponentFixture<CandidateDialogComponent>;
        let service: CandidateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateDialogComponent],
                    providers: [
                        UserService,
                        NationalityService,
                        GenderService,
                        MaritalStatusService,
                        JobCategoryService,
                        JobService,
                        ProfileCategoryService,
                        CorporateService,
                        VisaTypeService,
                        CandidateService
                    ]
                })
                    .overrideTemplate(CandidateDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateService);
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
                        const entity = new Candidate(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidate = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'candidateListModification', content: 'OK' });
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
                        const entity = new Candidate();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidate = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'candidateListModification', content: 'OK' });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
