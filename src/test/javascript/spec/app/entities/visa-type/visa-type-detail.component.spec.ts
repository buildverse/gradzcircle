/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { VisaTypeDetailComponent } from 'app/entities/visa-type/visa-type-detail.component';
import { VisaTypeService } from 'app/entities/visa-type/visa-type.service';
import { VisaType } from 'app/entities/visa-type/visa-type.model';

describe('Component Tests', () => {
    describe('VisaType Management Detail Component', () => {
        let comp: VisaTypeDetailComponent;
        let fixture: ComponentFixture<VisaTypeDetailComponent>;
        let service: VisaTypeService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [VisaTypeDetailComponent],
                    providers: [VisaTypeService]
                })
                    .overrideTemplate(VisaTypeDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(VisaTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VisaTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new VisaType(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.visaType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
