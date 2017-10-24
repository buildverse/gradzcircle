/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ErrorMessagesDetailComponent } from '../../../../../../main/webapp/app/entities/error-messages/error-messages-detail.component';
import { ErrorMessagesService } from '../../../../../../main/webapp/app/entities/error-messages/error-messages.service';
import { ErrorMessages } from '../../../../../../main/webapp/app/entities/error-messages/error-messages.model';

describe('Component Tests', () => {

    describe('ErrorMessages Management Detail Component', () => {
        let comp: ErrorMessagesDetailComponent;
        let fixture: ComponentFixture<ErrorMessagesDetailComponent>;
        let service: ErrorMessagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [ErrorMessagesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ErrorMessagesService,
                    JhiEventManager
                ]
            }).overrideTemplate(ErrorMessagesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ErrorMessagesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ErrorMessagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ErrorMessages(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.errorMessages).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
