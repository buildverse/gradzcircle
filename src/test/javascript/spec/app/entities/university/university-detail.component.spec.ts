/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UniversityDetailComponent } from '../../../../../../main/webapp/app/entities/university/university-detail.component';
import { UniversityService } from '../../../../../../main/webapp/app/entities/university/university.service';
import { University } from '../../../../../../main/webapp/app/entities/university/university.model';

describe('Component Tests', () => {

    describe('University Management Detail Component', () => {
        let comp: UniversityDetailComponent;
        let fixture: ComponentFixture<UniversityDetailComponent>;
        let service: UniversityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [UniversityDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UniversityService,
                    JhiEventManager
                ]
            }).overrideTemplate(UniversityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UniversityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UniversityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new University(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.university).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
