/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CollegeDetailComponent } from '../../../../../../main/webapp/app/entities/college/college-detail.component';
import { CollegeService } from '../../../../../../main/webapp/app/entities/college/college.service';
import { College } from '../../../../../../main/webapp/app/entities/college/college.model';

describe('Component Tests', () => {

    describe('College Management Detail Component', () => {
        let comp: CollegeDetailComponent;
        let fixture: ComponentFixture<CollegeDetailComponent>;
        let service: CollegeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CollegeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CollegeService,
                    JhiEventManager
                ]
            }).overrideTemplate(CollegeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CollegeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollegeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new College(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.college).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
