/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { VisaTypeComponent } from 'app/entities/visa-type/visa-type.component';
import { VisaTypeService } from 'app/entities/visa-type/visa-type.service';
import { VisaType } from 'app/entities/visa-type/visa-type.model';

describe('Component Tests', () => {
    describe('VisaType Management Component', () => {
        let comp: VisaTypeComponent;
        let fixture: ComponentFixture<VisaTypeComponent>;
        let service: VisaTypeService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [VisaTypeComponent],
                    providers: [VisaTypeService]
                })
                    .overrideTemplate(VisaTypeComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(VisaTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VisaTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new VisaType(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.visaTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
