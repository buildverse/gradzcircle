/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { IndustryComponent } from '../../../../../../main/webapp/app/entities/industry/industry.component';
import { IndustryService } from '../../../../../../main/webapp/app/entities/industry/industry.service';
import { Industry } from '../../../../../../main/webapp/app/entities/industry/industry.model';

describe('Component Tests', () => {

    describe('Industry Management Component', () => {
        let comp: IndustryComponent;
        let fixture: ComponentFixture<IndustryComponent>;
        let service: IndustryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [IndustryComponent],
                providers: [
                    IndustryService
                ]
            })
            .overrideTemplate(IndustryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IndustryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IndustryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Industry(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.industries[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
