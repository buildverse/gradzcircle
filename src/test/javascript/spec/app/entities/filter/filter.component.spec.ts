/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { FilterComponent } from '../../../../../../main/webapp/app/entities/filter/filter.component';
import { FilterService } from '../../../../../../main/webapp/app/entities/filter/filter.service';
import { Filter } from '../../../../../../main/webapp/app/entities/filter/filter.model';

describe('Component Tests', () => {

    describe('Filter Management Component', () => {
        let comp: FilterComponent;
        let fixture: ComponentFixture<FilterComponent>;
        let service: FilterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [FilterComponent],
                providers: [
                    FilterService
                ]
            })
            .overrideTemplate(FilterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FilterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FilterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Filter(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.filters[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
