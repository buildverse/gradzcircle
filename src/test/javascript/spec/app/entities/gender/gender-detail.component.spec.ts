/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GenderDetailComponent } from '../../../../../../main/webapp/app/entities/gender/gender-detail.component';
import { GenderService } from '../../../../../../main/webapp/app/entities/gender/gender.service';
import { Gender } from '../../../../../../main/webapp/app/entities/gender/gender.model';

describe('Component Tests', () => {

    describe('Gender Management Detail Component', () => {
        let comp: GenderDetailComponent;
        let fixture: ComponentFixture<GenderDetailComponent>;
        let service: GenderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [GenderDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GenderService,
                    JhiEventManager
                ]
            }).overrideTemplate(GenderDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GenderDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GenderService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Gender(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.gender).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
