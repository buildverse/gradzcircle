/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { FilterDetailComponent } from '../../../../../../main/webapp/app/entities/filter/filter-detail.component';
import { FilterService } from '../../../../../../main/webapp/app/entities/filter/filter.service';
import { Filter } from '../../../../../../main/webapp/app/entities/filter/filter.model';

describe('Component Tests', () => {

    describe('Filter Management Detail Component', () => {
        let comp: FilterDetailComponent;
        let fixture: ComponentFixture<FilterDetailComponent>;
        let service: FilterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [FilterDetailComponent],
                providers: [
                    FilterService
                ]
            })
            .overrideTemplate(FilterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FilterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FilterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Filter(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.filter).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
