/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEducationDialogComponent } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education-dialog.component';
import { CandidateEducationService } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.service';
import { CandidateEducation } from '../../../../../../main/webapp/app/entities/candidate-education/candidate-education.model';
import { CandidateService } from '../../../../../../main/webapp/app/entities/candidate';
import { QualificationService } from '../../../../../../main/webapp/app/entities/qualification';
import { CourseService } from '../../../../../../main/webapp/app/entities/course';
import { CollegeService } from '../../../../../../main/webapp/app/entities/college';

describe('Component Tests', () => {

    describe('CandidateEducation Management Dialog Component', () => {
        let comp: CandidateEducationDialogComponent;
        let fixture: ComponentFixture<CandidateEducationDialogComponent>;
        let service: CandidateEducationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateEducationDialogComponent],
                providers: [
                    CandidateService,
                    QualificationService,
                    CourseService,
                    CollegeService,
                    CandidateEducationService
                ]
            })
            .overrideTemplate(CandidateEducationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEducationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateEducationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateEducation(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.candidateEducation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'candidateEducationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateEducation();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.candidateEducation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'candidateEducationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
