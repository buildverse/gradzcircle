/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';
import { MockRouter } from '../../../helpers/mock-route.service';
import { GradzcircleTestModule } from '../../../test.module';
import { Router } from '@angular/router';
import { AppConfigDeleteDialogComponent } from 'app/entities/app-config/app-config-delete-dialog.component';
import { AppConfigService } from 'app/entities/app-config/app-config.service';

describe('Component Tests', () => {
    describe('AppConfig Management Delete Component', () => {
        let comp: AppConfigDeleteDialogComponent;
        let fixture: ComponentFixture<AppConfigDeleteDialogComponent>;
        let service: AppConfigService;
        let mockEventManager: any;
        let mockActiveModal: any;
        let mockRouter: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [AppConfigDeleteDialogComponent],
                    providers: [AppConfigService, { provide: Router, useClass: MockRouter }]
                })
                    .overrideTemplate(AppConfigDeleteDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(AppConfigDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppConfigService);
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
                        expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin', { outlets: { popup: null } }]);
                    })
                )
            );
            it(
                'Should call clear on user click cancel on modal',
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
