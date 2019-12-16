/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CountryDetailComponent } from 'app/entities/country/country-detail.component';
import { CountryService } from 'app/entities/country/country.service';
import { Country } from 'app/entities/country/country.model';

describe('Component Tests', () => {
    describe('Country Management Detail Component', () => {
        let comp: CountryDetailComponent;
        let fixture: ComponentFixture<CountryDetailComponent>;
        let service: CountryService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CountryDetailComponent],
                    providers: [CountryService]
                })
                    .overrideTemplate(CountryDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CountryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CountryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new Country(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.country).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
