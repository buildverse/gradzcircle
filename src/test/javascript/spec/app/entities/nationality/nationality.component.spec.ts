/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { NationalityComponent } from '../../../../../../main/webapp/app/entities/nationality/nationality.component';
import { NationalityService } from '../../../../../../main/webapp/app/entities/nationality/nationality.service';
import { Nationality } from '../../../../../../main/webapp/app/entities/nationality/nationality.model';

describe('Component Tests', () => {

    describe('Nationality Management Component', () => {
        let comp: NationalityComponent;
        let fixture: ComponentFixture<NationalityComponent>;
        let service: NationalityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [NationalityComponent],
                providers: [
                    NationalityService
                ]
            })
            .overrideTemplate(NationalityComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NationalityComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NationalityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Nationality(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.nationalities[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
