/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { StatesComponent } from '../../../../../../main/webapp/app/entities/states/states.component';
import { StatesService } from '../../../../../../main/webapp/app/entities/states/states.service';
import { States } from '../../../../../../main/webapp/app/entities/states/states.model';

describe('Component Tests', () => {

    describe('States Management Component', () => {
        let comp: StatesComponent;
        let fixture: ComponentFixture<StatesComponent>;
        let service: StatesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [StatesComponent],
                providers: [
                    StatesService
                ]
            })
            .overrideTemplate(StatesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StatesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StatesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new States(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.states[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
