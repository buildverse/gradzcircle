/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { FilterCategoryComponent } from '../../../../../../main/webapp/app/entities/filter-category/filter-category.component';
import { FilterCategoryService } from '../../../../../../main/webapp/app/entities/filter-category/filter-category.service';
import { FilterCategory } from '../../../../../../main/webapp/app/entities/filter-category/filter-category.model';

describe('Component Tests', () => {

    describe('FilterCategory Management Component', () => {
        let comp: FilterCategoryComponent;
        let fixture: ComponentFixture<FilterCategoryComponent>;
        let service: FilterCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [FilterCategoryComponent],
                providers: [
                    FilterCategoryService
                ]
            })
            .overrideTemplate(FilterCategoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FilterCategoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FilterCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new FilterCategory(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.filterCategories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
