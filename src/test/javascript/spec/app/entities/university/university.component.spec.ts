/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { UniversityComponent } from '../../../../../../main/webapp/app/entities/university/university.component';
import { UniversityService } from '../../../../../../main/webapp/app/entities/university/university.service';
import { University } from '../../../../../../main/webapp/app/entities/university/university.model';

describe('Component Tests', () => {

    describe('University Management Component', () => {
        let comp: UniversityComponent;
        let fixture: ComponentFixture<UniversityComponent>;
        let service: UniversityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [UniversityComponent],
                providers: [
                    UniversityService
                ]
            })
            .overrideTemplate(UniversityComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UniversityComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UniversityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new University(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.universities[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
