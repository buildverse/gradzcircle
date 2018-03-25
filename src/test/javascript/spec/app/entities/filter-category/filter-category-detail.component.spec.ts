/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FilterCategoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(FilterCategoryDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new FilterCategory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.filterCategory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
