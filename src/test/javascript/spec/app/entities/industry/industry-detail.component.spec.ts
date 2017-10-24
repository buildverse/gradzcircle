/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { IndustryDetailComponent } from '../../../../../../main/webapp/app/entities/industry/industry-detail.component';
import { IndustryService } from '../../../../../../main/webapp/app/entities/industry/industry.service';
import { Industry } from '../../../../../../main/webapp/app/entities/industry/industry.model';

describe('Component Tests', () => {

    describe('Industry Management Detail Component', () => {
        let comp: IndustryDetailComponent;
        let fixture: ComponentFixture<IndustryDetailComponent>;
        let service: IndustryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [IndustryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    IndustryService,
                    JhiEventManager
                ]
            }).overrideTemplate(IndustryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IndustryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IndustryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Industry(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.industry).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
