/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VisaTypeDetailComponent } from '../../../../../../main/webapp/app/entities/visa-type/visa-type-detail.component';
import { VisaTypeService } from '../../../../../../main/webapp/app/entities/visa-type/visa-type.service';
import { VisaType } from '../../../../../../main/webapp/app/entities/visa-type/visa-type.model';

describe('Component Tests', () => {

    describe('VisaType Management Detail Component', () => {
        let comp: VisaTypeDetailComponent;
        let fixture: ComponentFixture<VisaTypeDetailComponent>;
        let service: VisaTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [VisaTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VisaTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(VisaTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VisaTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VisaTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new VisaType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.visaType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
