/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmployabilityDetailComponent } from '../../../../../../main/webapp/app/entities/employability/employability-detail.component';
import { EmployabilityService } from '../../../../../../main/webapp/app/entities/employability/employability.service';
import { Employability } from '../../../../../../main/webapp/app/entities/employability/employability.model';

describe('Component Tests', () => {

    describe('Employability Management Detail Component', () => {
        let comp: EmployabilityDetailComponent;
        let fixture: ComponentFixture<EmployabilityDetailComponent>;
        let service: EmployabilityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [EmployabilityDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmployabilityService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmployabilityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployabilityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployabilityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Employability(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.employability).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
