/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CorporateDetailComponent } from '../../../../../../main/webapp/app/entities/corporate/corporate-detail.component';
import { CorporateService } from '../../../../../../main/webapp/app/entities/corporate/corporate.service';
import { Corporate } from '../../../../../../main/webapp/app/entities/corporate/corporate.model';

describe('Component Tests', () => {

    describe('Corporate Management Detail Component', () => {
        let comp: CorporateDetailComponent;
        let fixture: ComponentFixture<CorporateDetailComponent>;
        let service: CorporateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CorporateDetailComponent],
                providers: [
                    CorporateService
                ]
            })
            .overrideTemplate(CorporateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorporateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorporateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Corporate(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.corporate).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
