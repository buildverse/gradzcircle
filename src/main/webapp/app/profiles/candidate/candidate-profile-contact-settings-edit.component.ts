import { Component, OnInit, OnDestroy } from '@angular/core';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import 'rxjs/add/operator/debounceTime';
import { Observable } from 'rxjs/Observable';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { CountryService } from '../../entities/country/country.service';
import { Subscription } from 'rxjs';
import { Country } from '../../entities/country';
import { CandidateProfileSettingService } from './candidate-profile-setting.service';

/**
 * @TODO:
 *
 * 1. Update validations for type ahead need to ensure valid value is selected
 * 2. Clean up console logs
 * 3. Add config for url PROD vs DEV
 * 4. ADDRESS HANDLING IS VERY HACKY NEED TO FIX THIS AS PART OF UPLIFT.
 *
 */

// const URL = 'http://localhost:8080/api/upload'

@Component({
    moduleId: module.id,
    selector: 'jhi-candidate-contact-settings-edit',
    templateUrl: 'candidate-profile-contact-settings-edit.component.html'
})
export class CandidateContactSettingsEditComponent implements OnInit, OnDestroy {
    contactSettingForm: FormGroup;
    candidate: Candidate;
    errorMessage: String;
    subscription: Subscription;
    countries: Country[];
    profileSubscriber: Subscription;
    constructor(
        private formBuilder: FormBuilder,
        private candidateService: CandidateService,
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private countryService: CountryService,
        private candidateSettingService: CandidateProfileSettingService
    ) {}

    ngOnInit() {
        this.profileSubscriber = this.candidateSettingService
            .getCandidateFromParentToChild()
            .subscribe(candidate => (this.candidate = candidate));
        this.contactSettingForm = this.formBuilder.group({
            address: this.buildAddressGroup()
        });
        this.onCandidateRetrieved();
    }

    onCandidateRetrieved(): void {
        this.contactSettingForm.patchValue({
            address: {
                id: this.candidate.addresses[0] ? this.candidate.addresses[0].id : '',
                addressLineOne: this.candidate.addresses[0] ? this.candidate.addresses[0].addressLineOne : '',
                //   addressLineTwo: this.candidate.addresses[0] ? this.candidate.addresses[0].addressLineTwo : '',
                city: this.candidate.addresses[0] ? this.candidate.addresses[0].city : '',
                // state: this.candidate.addresses[0] ? this.candidate.addresses[0].state : '',
                country: this.candidate.addresses[0] ? this.candidate.addresses[0].country : '',
                // zip: this.candidate.addresses[0] ? this.candidate.addresses[0].zip : '',
                phoneCode: this.candidate.phoneCode ? this.candidate.phoneCode : '+91',
                phoneNumber: this.candidate.phoneNumber
            }
        });
    }

    buildAddressGroup(): FormGroup {
        return this.formBuilder.group({
            id: [null],
            addressLineOne: [null],
            // addressLineTwo: [null],
            city: [null, [Validators.required]],
            // state: [null, [Validators.required]],
            country: [null, [Validators.required]],
            //   zip: [null],
            phoneNumber: [null, [Validators.required, Validators.pattern('[0-9]+'), Validators.minLength(10), Validators.maxLength(10)]],
            phoneCode: ['+91', [Validators.required, Validators.pattern('[+.0-9]+')]]
        });
    }

    requestCountryData = (text: string): Observable<Response> => {
        return this.countryService
            .searchRemote({
                query: text
            })
            .map(data => data.body);
    };

    onCountrySelect(countrySelected) {
        if (!countrySelected) {
            this.contactSettingForm.get('phoneCode').reset();
        }
        for (const country of this.countries) {
            if (countrySelected) {
                if (country.id === countrySelected.id) {
                    this.contactSettingForm.get('phoneCode').setValue('+' + country.phoneCode);
                }
            }
        }
    }

    save(): void {
        if (this.contactSettingForm.dirty && this.contactSettingForm.valid) {
            const candidate = this.prepareCandidateBeforeSave();
            this.candidateService.update(candidate).subscribe(
                () => this.onSaveComplete(),
                (error: any) => {
                    this.errorMessage = <any>error;
                    this.contactSettingForm.reset();
                    this.router.navigate(['/error']);
                    return Observable.of(null);
                }
            );
        } else {
            if (!this.contactSettingForm.dirty) {
                this.onSaveComplete();
            }
        }
    }

    prepareCandidateBeforeSave(): any {
        const candidate = Object.assign({}, this.candidate, this.contactSettingForm.value);
        // candidate.jobCategories = this.reCreateJobCategoryModelFromSelection(candidate.jobCategories);
        this.setCandidateAddress(candidate);
        return candidate;
    }

    setCandidateAddress(candidate) {
        const addresses = new Array();
        const address = this.contactSettingForm.get('address').value;
        // address.id = this.candidate.addresses[0] ? this.candidate.addresses[0].id : undefined;
        addresses.push(address);
        candidate.addresses = addresses;
        // Move candidate phone to cnaidate object
        candidate.phoneCode = address.phoneCode;
        candidate.phoneNumber = address.phoneNumber;
    }

    onSaveComplete(): void {
        this.contactSettingForm.reset();
        this.eventManager.broadcast({ name: 'candidatePrimarySettingModification', content: 'OK' });
        this.candidateSettingService.changeSetting('contactSetting');
        this.router.navigate(['/', 'candidate-profile']);
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.eventManager.destroy(this.subscription);
        }
        if (this.profileSubscriber) {
            this.eventManager.destroy(this.profileSubscriber);
        }
    }
}
