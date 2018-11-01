/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CountryComponent } from '../../../../../../main/webapp/app/entities/country/country.component';
import { CountryService } from '../../../../../../main/webapp/app/entities/country/country.service';
import { Country } from '../../../../../../main/webapp/app/entities/country/country.model';

describe('Component Tests', () => {

    describe('Country Management Component', () => {
        let comp: CountryComponent;
        let fixture: ComponentFixture<CountryComponent>;
        let service: CountryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CountryComponent],
                providers: [
                    CountryService
                ]
            })
            .overrideTemplate(CountryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CountryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CountryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Country(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.countries[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
