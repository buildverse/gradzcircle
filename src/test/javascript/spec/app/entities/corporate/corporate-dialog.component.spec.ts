/* tslint:disable max-line-length */
import { UserService } from '../../../../../../main/webapp/app/core/user/user.service';
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GradzcircleTestModule } from '../../../test.module';
import { CorporateDialogComponent } from '../../../../../../main/webapp/app/entities/corporate/corporate-dialog.component';
import { CorporateService } from '../../../../../../main/webapp/app/entities/corporate/corporate.service';
import { Corporate } from '../../../../../../main/webapp/app/entities/corporate/corporate.model';
import { CountryService } from '../../../../../../main/webapp/app/entities/country';
import { IndustryService } from '../../../../../../main/webapp/app/entities/industry';

describe('Component Tests', () => {
    describe('Corporate Management Dialog Component', () => {
        let comp: CorporateDialogComponent;
        let fixture: ComponentFixture<CorporateDialogComponent>;
        let service: CorporateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CorporateDialogComponent],
                    providers: [CountryService, IndustryService, UserService, CorporateService]
                })
                    .overrideTemplate(CorporateDialogComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CorporateDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorporateService);
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
                        const entity = new Corporate(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.corporate = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'corporateListModification', content: 'OK' });
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
                        const entity = new Corporate();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                        comp.corporate = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'corporateListModification', content: 'OK' });
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
