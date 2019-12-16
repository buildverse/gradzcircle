/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { AuditDetailComponent } from 'app/entities/audit/audit-detail.component';
import { AuditService } from 'app/entities/audit/audit.service';
import { Audit } from 'app/entities/audit/audit.model';

describe('Component Tests', () => {
    describe('Audit Management Detail Component', () => {
        let comp: AuditDetailComponent;
        let fixture: ComponentFixture<AuditDetailComponent>;
        let service: AuditService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [AuditDetailComponent],
                    providers: [AuditService]
                })
                    .overrideTemplate(AuditDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(AuditDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuditService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new Audit(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.audit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
