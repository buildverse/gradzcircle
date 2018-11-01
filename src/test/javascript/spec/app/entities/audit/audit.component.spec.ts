/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { AuditComponent } from '../../../../../../main/webapp/app/entities/audit/audit.component';
import { AuditService } from '../../../../../../main/webapp/app/entities/audit/audit.service';
import { Audit } from '../../../../../../main/webapp/app/entities/audit/audit.model';

describe('Component Tests', () => {

    describe('Audit Management Component', () => {
        let comp: AuditComponent;
        let fixture: ComponentFixture<AuditComponent>;
        let service: AuditService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [AuditComponent],
                providers: [
                    AuditService
                ]
            })
            .overrideTemplate(AuditComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuditComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuditService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Audit(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.audits[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
