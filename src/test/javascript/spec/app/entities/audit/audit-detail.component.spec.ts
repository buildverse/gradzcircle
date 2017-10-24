/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AuditDetailComponent } from '../../../../../../main/webapp/app/entities/audit/audit-detail.component';
import { AuditService } from '../../../../../../main/webapp/app/entities/audit/audit.service';
import { Audit } from '../../../../../../main/webapp/app/entities/audit/audit.model';

describe('Component Tests', () => {

    describe('Audit Management Detail Component', () => {
        let comp: AuditDetailComponent;
        let fixture: ComponentFixture<AuditDetailComponent>;
        let service: AuditService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [AuditDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AuditService,
                    JhiEventManager
                ]
            }).overrideTemplate(AuditDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuditDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuditService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Audit(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.audit).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
