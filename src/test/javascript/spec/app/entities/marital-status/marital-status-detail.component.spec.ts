/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MaritalStatusDetailComponent } from '../../../../../../main/webapp/app/entities/marital-status/marital-status-detail.component';
import { MaritalStatusService } from '../../../../../../main/webapp/app/entities/marital-status/marital-status.service';
import { MaritalStatus } from '../../../../../../main/webapp/app/entities/marital-status/marital-status.model';

describe('Component Tests', () => {

    describe('MaritalStatus Management Detail Component', () => {
        let comp: MaritalStatusDetailComponent;
        let fixture: ComponentFixture<MaritalStatusDetailComponent>;
        let service: MaritalStatusService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [MaritalStatusDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MaritalStatusService,
                    JhiEventManager
                ]
            }).overrideTemplate(MaritalStatusDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MaritalStatusDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaritalStatusService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MaritalStatus(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.maritalStatus).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
