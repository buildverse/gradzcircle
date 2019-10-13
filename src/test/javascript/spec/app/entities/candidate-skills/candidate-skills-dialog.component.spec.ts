/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateSkillsDialogComponent } from '../../../../../../main/webapp/app/entities/candidate-skills/candidate-skills-dialog.component';
import { CandidateSkillsService } from '../../../../../../main/webapp/app/entities/candidate-skills/candidate-skills.service';
import { CandidateSkills } from '../../../../../../main/webapp/app/entities/candidate-skills/candidate-skills.model';
import { CandidateService } from '../../../../../../main/webapp/app/entities/candidate';
import { SkillsService } from '../../../../../../main/webapp/app/entities/skills';

describe('Component Tests', () => {

    describe('CandidateSkills Management Dialog Component', () => {
        let comp: CandidateSkillsDialogComponent;
        let fixture: ComponentFixture<CandidateSkillsDialogComponent>;
        let service: CandidateSkillsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateSkillsDialogComponent],
                providers: [
                    CandidateService,
                    SkillsService,
                    CandidateSkillsService
                ]
            })
            .overrideTemplate(CandidateSkillsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateSkillsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateSkillsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateSkills(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.candidateSkills = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'candidateSkillsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateSkills();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.candidateSkills = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'candidateSkillsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
