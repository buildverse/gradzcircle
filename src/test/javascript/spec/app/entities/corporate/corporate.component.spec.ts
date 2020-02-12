/* tslint:disable max-line-length */
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from 'app/core';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CorporateComponent } from 'app/entities/corporate/corporate.component';
import { CorporateService } from 'app/entities/corporate/corporate.service';
import { Corporate } from 'app/entities/corporate/corporate.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';

describe('Component Tests', () => {
    describe('Corporate Management Component', () => {
        let comp: CorporateComponent;
        let fixture: ComponentFixture<CorporateComponent>;
        let service: CorporateService;
        let storageService: DataStorageService;
        let mockPrincipal: any;
        let ngxSpinnerService: NgxSpinnerService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CorporateComponent],
                    providers: [
                        CorporateService,
                        LocalStorageService,
                        DataStorageService,
                        NgxSpinnerService,
                        { provide: Principal, useClass: MockPrincipal }
                    ]
                })
                    .overrideTemplate(CorporateComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CorporateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorporateService);
            storageService = fixture.debugElement.injector.get(DataStorageService);
            mockPrincipal = fixture.debugElement.injector.get(Principal);
            ngxSpinnerService = fixture.debugElement.injector.get(NgxSpinnerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                const account = { authorities: ['ROLE_ADMIN'] };
                mockPrincipal.setResponse(account);
                spyOn(storageService, 'getData').and.returnValue('ROLE_ADMIN');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Corporate(123)],
                            headers
                        })
                    )
                );
                fixture.detectChanges();

                // WHEN
                comp.ngOnInit();

                // THEN
                Promise.resolve().then(() => {
                    expect(service.query).toHaveBeenCalled();
                    expect(comp.corporates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                });
            });
        });
    });
});
