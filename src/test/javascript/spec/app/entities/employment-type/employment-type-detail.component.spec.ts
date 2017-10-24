/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmploymentTypeDetailComponent } from '../../../../../../main/webapp/app/entities/employment-type/employment-type-detail.component';
import { EmploymentTypeService } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.service';
import { EmploymentType } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.model';

describe('Component Tests', () => {

    describe('EmploymentType Management Detail Component', () => {
        let comp: EmploymentTypeDetailComponent;
        let fixture: ComponentFixture<EmploymentTypeDetailComponent>;
        let service: EmploymentTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [EmploymentTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmploymentTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmploymentTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmploymentTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmploymentTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EmploymentType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.employmentType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
