/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { FilterCategoryDetailComponent } from '../../../../../../main/webapp/app/entities/filter-category/filter-category-detail.component';
import { FilterCategoryService } from '../../../../../../main/webapp/app/entities/filter-category/filter-category.service';
import { FilterCategory } from '../../../../../../main/webapp/app/entities/filter-category/filter-category.model';

describe('Component Tests', () => {

    describe('FilterCategory Management Detail Component', () => {
        let comp: FilterCategoryDetailComponent;
        let fixture: ComponentFixture<FilterCategoryDetailComponent>;
        let service: FilterCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [FilterCategoryDetailComponent],
                providers: [
                    FilterCategoryService
                ]
            })
            .overrideTemplate(FilterCategoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FilterCategoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FilterCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new FilterCategory(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.filterCategory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
