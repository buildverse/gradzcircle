/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { ErrorMessagesComponent } from 'app/entities/error-messages/error-messages.component';
import { ErrorMessagesService } from 'app/entities/error-messages/error-messages.service';
import { ErrorMessages } from 'app/entities/error-messages/error-messages.model';

describe('Component Tests', () => {
    describe('ErrorMessages Management Component', () => {
        let comp: ErrorMessagesComponent;
        let fixture: ComponentFixture<ErrorMessagesComponent>;
        let service: ErrorMessagesService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [ErrorMessagesComponent],
                    providers: [ErrorMessagesService]
                })
                    .overrideTemplate(ErrorMessagesComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(ErrorMessagesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ErrorMessagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new ErrorMessages(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.errorMessages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
