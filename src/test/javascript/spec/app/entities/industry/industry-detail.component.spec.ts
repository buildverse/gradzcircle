/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { IndustryDetailComponent } from 'app/entities/industry/industry-detail.component';
import { IndustryService } from 'app/entities/industry/industry.service';
import { Industry } from 'app/entities/industry/industry.model';

describe('Component Tests', () => {
    describe('Industry Management Detail Component', () => {
        let comp: IndustryDetailComponent;
        let fixture: ComponentFixture<IndustryDetailComponent>;
        let service: IndustryService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [IndustryDetailComponent],
                    providers: [IndustryService]
                })
                    .overrideTemplate(IndustryDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(IndustryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IndustryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new Industry(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.industry).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
