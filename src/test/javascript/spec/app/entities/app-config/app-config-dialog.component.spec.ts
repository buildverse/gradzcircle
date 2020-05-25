/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';
import { MockRouter } from '../../../helpers/mock-route.service';
import { GradzcircleTestModule } from '../../../test.module';
import { HttpErrorResponse } from '@angular/common/http';
import { AppConfigDialogComponent } from 'app/entities/app-config/app-config-dialog.component';
import { AppConfigService } from 'app/entities/app-config/app-config.service';
import { AppConfig } from 'app/entities/app-config/app-config.model';
import { Router } from '@angular/router';
describe('Component Tests', () => {
    describe('AppConfig Management Dialog Component', () => {
        let comp: AppConfigDialogComponent;
        let fixture: ComponentFixture<AppConfigDialogComponent>;
        let service: AppConfigService;
        let mockEventManager: any;
        let mockActiveModal: any;
        let mockRouter: any;
        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [AppConfigDialogComponent],
                    providers: [AppConfigService, { provide: Router, useClass: MockRouter }]
                })
                    .overrideTemplate(AppConfigDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(AppConfigDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppConfigService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
            mockRouter = fixture.debugElement.injector.get(Router);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AppConfig(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.appConfig = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'appConfigListModification', content: 'OK' });
                        expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin', { outlets: { popup: null } }]);
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
                        const entity = new AppConfig();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.appConfig = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'appConfigListModification', content: 'OK' });
                        expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin', { outlets: { popup: null } }]);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
            it(
                'Should call clear on user click cancel on modal',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'create').and.returnValue(Observable.of({}));
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
