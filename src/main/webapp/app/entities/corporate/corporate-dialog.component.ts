import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Corporate} from './corporate.model';
import {CorporatePopupService} from './corporate-popup.service';
import {CorporateService} from './corporate.service';
import {Country, CountryService} from '../country';
import {Industry, IndustryService} from '../industry';
import {User, UserService} from '../../shared';
import {EditorProperties, DataService} from '../../shared';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'jhi-corporate-dialog',
  templateUrl: './corporate-dialog.component.html'
})
export class CorporateDialogComponent implements OnInit {

  corporate: Corporate;
  isSaving: boolean;
  editorConfig: any;
  countries: Country[];
  industries: Industry[];
  users: User[];
  establishedSinceDp: any;


  constructor(
    public activeModal: NgbActiveModal,
    private jhiAlertService: JhiAlertService,
    private corporateService: CorporateService,
    private countryService: CountryService,
    private industryService: IndustryService,
    private eventManager: JhiEventManager,
    private userService: UserService,
    private dataService: DataService,
    private spinnerService: NgxSpinnerService
  ) {
  }

  ngOnInit() {
    this.editorConfig = {
      'toolbarGroups': [
        {'name': 'editing', 'groups': ['find', 'selection', 'spellchecker', 'editing']},
        {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
        {name: 'paragraph', groups: ['list', 'indent', 'align']},
      ],
      'removeButtons': 'Source,Save,Templates,Find,Replace,Scayt,SelectAll,forms'
    };
    this.isSaving = false;
    this.countryService.query()
      .subscribe((res: HttpResponse<Country[]>) => {this.countries = res.body;}, (res: HttpErrorResponse) => this.onError(res.message));
    this.industryService.query()
      .subscribe((res: HttpResponse<Industry[]>) => {this.industries = res.body;}, (res: HttpErrorResponse) => this.onError(res.message));
    this.userService.query()
      .subscribe((res: HttpResponse<User[]>) => {this.users = res.body;}, (res: HttpErrorResponse) => this.onError(res.message));
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  save() {
    this.spinnerService.show();
    this.isSaving = true;
    if (this.corporate.id !== undefined) {
      this.subscribeToSaveResponse(
        this.corporateService.update(this.corporate));
    } else {
      this.subscribeToSaveResponse(
        this.corporateService.create(this.corporate));
    }
  }

  requestCountryData = (text: string): Observable<Response> => {
    return this.countryService.searchRemote({
      query: text
    }).map((data) => data.body);
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<Corporate>>) {
    result.subscribe((res: HttpResponse<Corporate>) =>
      this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess(result: Corporate) {
    this.eventManager.broadcast({name: 'corporateListModification', content: 'OK'});
    this.isSaving = false;
    this.spinnerService.hide();
    this.activeModal.dismiss(result);
  }

  private onSaveError() {
    this.spinnerService.hide();
    this.isSaving = false;
  }

  private onError(error: any) {
    this.jhiAlertService.error(error.message, null, null);
  }

  trackCountryById(index: number, item: Country) {
    return item.id;
  }

  trackIndustryById(index: number, item: Industry) {
    return item.id;
  }

  trackUserById(index: number, item: User) {
    return item.id;
  }
}

@Component({
  selector: 'jhi-corporate-popup',
  template: ''
})
export class CorporatePopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private corporatePopupService: CorporatePopupService,
    private dataService: DataService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.corporatePopupService
          .open(CorporateDialogComponent as Component, params['id']);
      } else {
        const id = this.dataService.getRouteData();
        if (id) {
          this.corporatePopupService
            .open(CorporateDialogComponent as Component, id);
        } else {
          this.corporatePopupService
            .open(CorporateDialogComponent as Component);
        }
      }
    });
  }

  ngOnDestroy() {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }
}
