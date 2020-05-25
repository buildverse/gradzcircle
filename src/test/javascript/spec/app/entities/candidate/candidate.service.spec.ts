/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JhiDateUtils } from 'ng-jhipster';

import { CandidateService } from 'app/entities/candidate/candidate.service';
import { Candidate } from 'app/entities/candidate/candidate.model';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
    describe('Candidate Service', () => {
        let injector: TestBed;
        let service: CandidateService;
        let httpMock: HttpTestingController;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule],
                providers: [JhiDateUtils, CandidateService]
            });
            injector = getTestBed();
            service = injector.get(CandidateService);
            httpMock = injector.get(HttpTestingController);
        });

        describe('Service methods', () => {
            it('should call correct get URL', () => {
                service.find(123).subscribe(() => {});

                const req = httpMock.expectOne({ method: 'GET' });

                const resourceUrl = SERVER_API_URL + 'api/candidates';
                expect(req.request.url).toEqual(resourceUrl + '/' + 123);
            });
            it('should return Candidate', () => {
                service.find(123).subscribe(received => {
                    expect(received.body.id).toEqual(123);
                });

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should propagate not found response', () => {
                service.find(123).subscribe(null, (_error: any) => {
                    expect(_error.status).toEqual(404);
                });

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush('Invalid request parameters', {
                    status: 404,
                    statusText: 'Bad Request'
                });
            });

            it('should call correct create URL', () => {
                service.create(new Candidate(null, 'abhina', 'prakash')).subscribe(() => {});

                const req = httpMock.expectOne({ method: 'POST' });

                const resourceUrl = SERVER_API_URL + 'api/candidates';
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should create candidate', () => {
                const resourceUrl = SERVER_API_URL + 'api/candidates';
                service.create(new Candidate(null, 'abhinav', 'prakash')).subscribe(recieved => {
                    expect(recieved.body.firstName).toEqual('abhinav');
                    expect(recieved.body.lastName).toEqual('prakash');
                });
                httpMock.expectOne(resourceUrl).flush({ firstName: 'abhinav', lastName: 'prakash' });
            });

            it('should call correct update URL', () => {
                service.update(new Candidate(null, 'abhina', 'prakash')).subscribe(() => {});

                const req = httpMock.expectOne({ method: 'PUT' });

                const resourceUrl = SERVER_API_URL + 'api/candidates';
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should update candidate', () => {
                const resourceUrl = SERVER_API_URL + 'api/candidates';
                service.update(new Candidate(null, 'abhinav', 'prakash')).subscribe(recieved => {
                    expect(recieved.body.firstName).toEqual('abhinav');
                    expect(recieved.body.lastName).toEqual('prakash');
                });
                httpMock.expectOne(resourceUrl).flush({ firstName: 'abhinav', lastName: 'prakash' });
            });

            it('should get Linked Candidate for a corporate job', () => {
                const resourceUrl = SERVER_API_URL + 'api/candidates';
                service.update(new Candidate(null, 'abhinav', 'prakash')).subscribe(recieved => {
                    expect(recieved.body.firstName).toEqual('abhinav');
                    expect(recieved.body.lastName).toEqual('prakash');
                });
                httpMock.expectOne(resourceUrl).flush({ firstName: 'abhinav', lastName: 'prakash' });
            });

            it('should get Linked Candidate for a corporate job must call correct URL', () => {
                service.linkCandidateAndCorporateForJob(123, 345, 456).subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = 'api/candidate-corporate-link';
                expect(req.request.url).toEqual(resourceUrl + '/' + 123 + '/' + 345 + '/' + 456);
            });

            it('should return linked candidates when Linked Candidate for a corporate job is called ', () => {
                service.linkCandidateAndCorporateForJob(123, 345, 456).subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should get candidate by Login Id must call correct URL', () => {
                service.getCandidateByLoginId('123').subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = 'api/candidateByLogin';
                expect(req.request.url).toEqual(resourceUrl + '/' + 123);
            });

            it('should return candidate when candidate by Login Id  is called ', () => {
                service.getCandidateByLoginId('123').subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });
            it('should get candidate by Candidate Id must call correct URL', () => {
                service.getCandidateByCandidateId('123').subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = SERVER_API_URL + 'api/candidateById';
                expect(req.request.url).toEqual(resourceUrl + '/' + 123);
            });

            it('should return candidate when candidate by candidate Id  is called ', () => {
                service.getCandidateByCandidateId('123').subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should get candidateDetails must call correct URL', () => {
                service.getCandidateDetails('123').subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = SERVER_API_URL + 'api/candidateDetails';
                expect(req.request.url).toEqual(resourceUrl + '/' + 123);
            });

            it('should return candidate when candidate by candidate Id  is called ', () => {
                service.getCandidateDetails('123').subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should get candidatePublicProfile must call correct URL', () => {
                service.getCandidatePublicProfile(123, 456, 789).subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = 'api/candidates/candidatePublicProfile';
                expect(req.request.url).toEqual(resourceUrl + '/' + 123 + '/' + 456 + '/' + 789);
            });

            it('should return candidate when candidatePublicProfile  is called ', () => {
                service.getCandidatePublicProfile(123, 456, 789).subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should query must call correct URL', () => {
                service.query('page: 0, size: 1, sort: something').subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = SERVER_API_URL + 'api/candidates';
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should return candidate List when query  is called ', () => {
                service.query('page: 0, size: 1, sort: something').subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should query for guest must call correct URL', () => {
                service.queryForGuest('page: 0, size: 1, sort: something').subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = 'api/candidatesPreview';
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should return candidate List when query For Guest  is called ', () => {
                service.queryForGuest('page: 0, size: 1, sort: something').subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });

            it('should delete  must call correct URL', () => {
                service.delete(123).subscribe(() => {});
                const req = httpMock.expectOne({ method: 'DELETE' });
                const resourceUrl = SERVER_API_URL + 'api/candidates/' + 123;
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should return nothing when delete  is called ', () => {
                service.delete(123).subscribe(() => received => {
                    expect(received.body.id).toEqual(null);
                });
                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ id: 123 });
            });

            it('should delete image be called  must call correct URL', () => {
                service.deleteImage(123).subscribe(() => {});
                const req = httpMock.expectOne({ method: 'DELETE' });
                const resourceUrl = 'api/remove/' + 123;
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should return nothing when delete  is called ', () => {
                service.deleteImage(123).subscribe(() => received => {
                    expect(received.body.id).toEqual(null);
                });
                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ id: 123 });
            });

            it('should search  be called  must call correct URL', () => {
                service.search('id:123').subscribe(() => {});
                const req = httpMock.expectOne({ method: 'GET' });
                const resourceUrl = SERVER_API_URL + 'api/_search/candidates';
                expect(req.request.url).toEqual(resourceUrl);
            });

            it('should return nothing when delete  is called ', () => {
                service.search('id:123').subscribe(() => received => {
                    expect(received.body.id).toEqual(123);
                });
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush({ id: 123 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
