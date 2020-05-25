/* tslint:disable max-line-length */
import { CandidatePreviewComponent } from 'app/entities/candidate/candidate-preview.component';
import { CandidateList } from 'app/entities/job/candidate-list.model';
import { DataStorageService } from 'app/shared/helper/localstorage.service';
import { MockRouter } from '../../../helpers/mock-route.service';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { Router, ActivatedRoute, Data } from '@angular/router';
import { CandidateService } from 'app/entities/candidate/candidate.service';
import { Candidate } from 'app/entities/candidate/candidate.model';
import { NgxSpinnerService } from 'ngx-spinner';
import { LocalStorageService } from 'ngx-webstorage';
import { of } from 'rxjs';

describe('Component Tests', () => {
    describe('Candidate Management Component', () => {
        let comp: CandidatePreviewComponent;
        let fixture: ComponentFixture<CandidatePreviewComponent>;
        let service: CandidateService;
        let spinnerService: NgxSpinnerService;
        let storageService: DataStorageService;
        let mockRouter: Router;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CandidatePreviewComponent],
                    providers: [
                        CandidateService,
                        NgxSpinnerService,
                        DataStorageService,
                        LocalStorageService,
                        { provide: Router, useClass: MockRouter },
                        {
                            provide: ActivatedRoute,
                            useValue: {
                                data: {
                                    subscribe: (fn: (value: Data) => void) =>
                                        fn({
                                            pagingParams: {
                                                predicate: 'id',
                                                reverse: false,
                                                page: 0
                                            }
                                        })
                                }
                            }
                        }
                    ]
                })
                    .overrideTemplate(CandidatePreviewComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidatePreviewComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateService);
            spinnerService = fixture.debugElement.injector.get(NgxSpinnerService);
            storageService = fixture.debugElement.injector.get(DataStorageService);
            mockRouter = fixture.debugElement.injector.get(Router);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'queryForGuest').and.returnValue(
                    of(
                        new HttpResponse({
                            body: [new CandidateList(123), new CandidateList(456)],
                            headers
                        })
                    )
                );

                spyOn(spinnerService, 'show').and.returnValue(null);
                spyOn(spinnerService, 'hide').and.returnValue(null);

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.queryForGuest).toHaveBeenCalled();
                expect(spinnerService.show).toHaveBeenCalled();
                expect(spinnerService.hide).toHaveBeenCalled();
                expect(comp.candidateList.length).toEqual(2);
                expect(comp.candidateList[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                expect(comp.candidateList[1]).toEqual(jasmine.objectContaining({ id: 456 }));
            });

            it('should load a page', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'queryForGuest').and.returnValue(
                    of(
                        new HttpResponse({
                            body: [new CandidateList(123), new CandidateList(456)],
                            headers
                        })
                    )
                );
                spyOn(spinnerService, 'show').and.returnValue(null);
                spyOn(spinnerService, 'hide').and.returnValue(null);
                // WHEN
                comp.loadPage(1);

                // THEN

                expect(service.queryForGuest).toHaveBeenCalled();
                expect(spinnerService.show).toHaveBeenCalled();
                expect(spinnerService.hide).toHaveBeenCalled();
                expect(comp.candidateList.length).toEqual(2);
                expect(comp.candidateList[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                expect(comp.candidateList[1]).toEqual(jasmine.objectContaining({ id: 456 }));
                expect(mockRouter.navigate).toHaveBeenCalledWith(['/candidatePreview'], {
                    queryParams: { page: 0, search: '', size: 20, sort: 'id,desc' }
                });
            });

            it('should not load a page is the page is the same as the previous page', () => {
                spyOn(service, 'queryForGuest').and.callThrough();
                // WHEN
                comp.loadPage(0);

                // THEN
                expect(service.queryForGuest).toHaveBeenCalledTimes(0);
            });

            it('should re-initialize the page', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'queryForGuest').and.returnValue(
                    of(
                        new HttpResponse({
                            body: [new CandidateList(123), new CandidateList(456)],
                            headers
                        })
                    )
                );
                spyOn(spinnerService, 'show').and.returnValue(null);
                spyOn(spinnerService, 'hide').and.returnValue(null);
                // WHEN
                comp.loadPage(1);
                comp.clear();
                // THEN
                expect(comp.page).toEqual(0);
                expect(service.queryForGuest).toHaveBeenCalledTimes(2);
                expect(spinnerService.show).toHaveBeenCalled();
                expect(spinnerService.hide).toHaveBeenCalled();
                expect(comp.candidateList.length).toEqual(2);
                expect(comp.candidateList[0]).toEqual(jasmine.objectContaining({ id: 123 }));
                expect(comp.candidateList[1]).toEqual(jasmine.objectContaining({ id: 456 }));
                expect(mockRouter.navigate).toHaveBeenCalledWith(['/candidatePreview'], {
                    queryParams: { page: 0, search: '', size: 20, sort: 'id,desc' }
                });
            });
            it('should calculate the sort attribute for an id', () => {
                // WHEN
                const result = comp.sort();

                // THEN
                expect(result).toEqual(['id,desc']);
            });
            it('should calculate the sort attribute for a non-id attribute', () => {
                // GIVEN
                comp.predicate = 'name';

                // WHEN
                const result = comp.sort();

                // THEN
                expect(result).toEqual(['name,desc', 'id']);
            });
        });
    });
});
