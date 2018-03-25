/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FilterService,
                    JhiEventManager
                ]
            }).overrideTemplate(FilterDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new Filter(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.filter).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
