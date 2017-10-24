/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { QualificationDetailComponent } from '../../../../../../main/webapp/app/entities/qualification/qualification-detail.component';
import { QualificationService } from '../../../../../../main/webapp/app/entities/qualification/qualification.service';
import { Qualification } from '../../../../../../main/webapp/app/entities/qualification/qualification.model';

describe('Component Tests', () => {

    describe('Qualification Management Detail Component', () => {
        let comp: QualificationDetailComponent;
        let fixture: ComponentFixture<QualificationDetailComponent>;
        let service: QualificationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [QualificationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    QualificationService,
                    JhiEventManager
                ]
            }).overrideTemplate(QualificationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(QualificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QualificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Qualification(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.qualification).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
