/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { StatesDetailComponent } from 'app/entities/states/states-detail.component';
import { StatesService } from 'app/entities/states/states.service';
import { States } from 'app/entities/states/states.model';

describe('Component Tests', () => {
    describe('States Management Detail Component', () => {
        let comp: StatesDetailComponent;
        let fixture: ComponentFixture<StatesDetailComponent>;
        let service: StatesService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [StatesDetailComponent],
                    providers: [StatesService]
                })
                    .overrideTemplate(StatesDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(StatesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StatesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new States(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.states).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
