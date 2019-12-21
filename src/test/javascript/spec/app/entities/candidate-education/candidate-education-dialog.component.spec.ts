/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';
import { LocalStorageService } from 'ngx-webstorage';
import { GradzcircleTestModule } from '../../../test.module';
import { CandidateEducationDialogComponent } from 'app/entities/candidate-education/candidate-education-dialog.component';
import { CandidateEducationService } from 'app/entities/candidate-education/candidate-education.service';
import { CandidateEducation } from 'app/entities/candidate-education/candidate-education.model';
import { CandidateService } from 'app/entities/candidate';
import { QualificationService } from 'app/entities/qualification';
import { CourseService } from 'app/entities/course';
import { CollegeService } from 'app/entities/college';
import { UniversityService } from 'app/entities/university';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModalRef, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

describe('Component Tests', () => {
    describe('CandidateEducation Management Dialog Component', () => {
        let comp: CandidateEducationDialogComponent;
        let fixture: ComponentFixture<CandidateEducationDialogComponent>;
        let candidateService: CandidateService;
        let qualifcationService: QualificationService;
        let courseService: CourseService;
        let collegeService: CollegeService;
        let candidateEducationService: CandidateEducationService;
        let universityService: UniversityService;
        let storageService: DataStorageService;
        let mockEventManager: any;
        let mockActiveModal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidateEducationDialogComponent],
                    providers: [
                        CandidateService,
                        QualificationService,
                        CourseService,
                        CollegeService,
                        CandidateEducationService,
                        UniversityService,
                        DataStorageService,
                        NgxSpinnerService,
                        LocalStorageService,
                        NgbDatepickerConfig
                    ]
                })
                    .overrideTemplate(CandidateEducationDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateEducationDialogComponent);
            comp = fixture.componentInstance;
            candidateEducationService = fixture.debugElement.injector.get(CandidateEducationService);
            candidateService = fixture.debugElement.injector.get(CandidateService);
            qualifcationService = fixture.debugElement.injector.get(QualificationService);
            courseService = fixture.debugElement.injector.get(CourseService);
            collegeService = fixture.debugElement.injector.get(CollegeService);
            universityService = fixture.debugElement.injector.get(UniversityService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
            storageService = fixture.debugElement.injector.get(DataStorageService);
            ngxSpinnerService = fixture.debugElement.injector.get(NgxSpinnerService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CandidateEducation(123);
                        spyOn(candidateEducationService, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateEducation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(candidateEducationService.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateEducationListModification',
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
                        const entity = new CandidateEducation();
                        spyOn(candidateEducationService, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.candidateEducation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(candidateEducationService.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
                            name: 'candidateEducationListModification',
                            content: 'OK'
                        });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
