/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CorporateComponent } from 'app/entities/corporate/corporate.component';
import { CorporateService } from 'app/entities/corporate/corporate.service';
import { Corporate } from 'app/entities/corporate/corporate.model';

describe('Component Tests', () => {
    describe('Corporate Management Component', () => {
        let comp: CorporateComponent;
        let fixture: ComponentFixture<CorporateComponent>;
        let service: CorporateService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CorporateComponent],
                    providers: [CorporateService]
                })
                    .overrideTemplate(CorporateComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CorporateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorporateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Corporate(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.corporates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
