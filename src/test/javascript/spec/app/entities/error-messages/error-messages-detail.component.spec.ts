/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
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
                    ErrorMessagesService
                ]
            })
            .overrideTemplate(ErrorMessagesDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ErrorMessages(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.errorMessages).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
