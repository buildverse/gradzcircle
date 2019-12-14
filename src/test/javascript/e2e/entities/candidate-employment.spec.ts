import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CandidateEmployment e2e test', () => {
    let navBarPage: NavBarPage;
    let candidateEmploymentDialogPage: CandidateEmploymentDialogPage;
    let candidateEmploymentComponentsPage: CandidateEmploymentComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateEmployments', () => {
        navBarPage.goToEntity('candidate-employment');
        candidateEmploymentComponentsPage = new CandidateEmploymentComponentsPage();
        expect(candidateEmploymentComponentsPage.getTitle()).toMatch(/gradzcircleApp.candidateEmployment.home.title/);
    });

    it('should load create CandidateEmployment dialog', () => {
        candidateEmploymentComponentsPage.clickOnCreateButton();
        candidateEmploymentDialogPage = new CandidateEmploymentDialogPage();
        expect(candidateEmploymentDialogPage.getModalTitle()).toMatch(/gradzcircleApp.candidateEmployment.home.createOrEditLabel/);
        candidateEmploymentDialogPage.close();
    });

    it('should create and save CandidateEmployments', () => {
        candidateEmploymentComponentsPage.clickOnCreateButton();
        candidateEmploymentDialogPage.setLocationInput('5');
        expect(candidateEmploymentDialogPage.getLocationInput()).toMatch('5');
        candidateEmploymentDialogPage.setJobTitleInput('jobTitle');
        expect(candidateEmploymentDialogPage.getJobTitleInput()).toMatch('jobTitle');
        candidateEmploymentDialogPage.setEmployerNameInput('employerName');
        expect(candidateEmploymentDialogPage.getEmployerNameInput()).toMatch('employerName');
        candidateEmploymentDialogPage.setEmploymentStartDateInput('2000-12-31');
        expect(candidateEmploymentDialogPage.getEmploymentStartDateInput()).toMatch('2000-12-31');
        candidateEmploymentDialogPage.setEmploymentEndDateInput('2000-12-31');
        expect(candidateEmploymentDialogPage.getEmploymentEndDateInput()).toMatch('2000-12-31');
        candidateEmploymentDialogPage.setEmploymentDurationInput('5');
        expect(candidateEmploymentDialogPage.getEmploymentDurationInput()).toMatch('5');
        candidateEmploymentDialogPage
            .getIsCurrentEmploymentInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    candidateEmploymentDialogPage.getIsCurrentEmploymentInput().click();
                    expect(candidateEmploymentDialogPage.getIsCurrentEmploymentInput().isSelected()).toBeFalsy();
                } else {
                    candidateEmploymentDialogPage.getIsCurrentEmploymentInput().click();
                    expect(candidateEmploymentDialogPage.getIsCurrentEmploymentInput().isSelected()).toBeTruthy();
                }
            });
        candidateEmploymentDialogPage.setJobDescriptionInput('jobDescription');
        expect(candidateEmploymentDialogPage.getJobDescriptionInput()).toMatch('jobDescription');
        candidateEmploymentDialogPage.candidateSelectLastOption();
        candidateEmploymentDialogPage.employmentTypeSelectLastOption();
        candidateEmploymentDialogPage.countrySelectLastOption();
        candidateEmploymentDialogPage.jobTypeSelectLastOption();
        candidateEmploymentDialogPage.save();
        expect(candidateEmploymentDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateEmploymentComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-employment div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateEmploymentDialogPage {
    modalTitle = element(by.css('h4#myCandidateEmploymentLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    locationInput = element(by.css('input#field_location'));
    jobTitleInput = element(by.css('input#field_jobTitle'));
    employerNameInput = element(by.css('input#field_employerName'));
    employmentStartDateInput = element(by.css('input#field_employmentStartDate'));
    employmentEndDateInput = element(by.css('input#field_employmentEndDate'));
    employmentDurationInput = element(by.css('input#field_employmentDuration'));
    isCurrentEmploymentInput = element(by.css('input#field_isCurrentEmployment'));
    jobDescriptionInput = element(by.css('input#field_jobDescription'));
    candidateSelect = element(by.css('select#field_candidate'));
    employmentTypeSelect = element(by.css('select#field_employmentType'));
    countrySelect = element(by.css('select#field_country'));
    jobTypeSelect = element(by.css('select#field_jobType'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setLocationInput = function(location) {
        this.locationInput.sendKeys(location);
    };

    getLocationInput = function() {
        return this.locationInput.getAttribute('value');
    };

    setJobTitleInput = function(jobTitle) {
        this.jobTitleInput.sendKeys(jobTitle);
    };

    getJobTitleInput = function() {
        return this.jobTitleInput.getAttribute('value');
    };

    setEmployerNameInput = function(employerName) {
        this.employerNameInput.sendKeys(employerName);
    };

    getEmployerNameInput = function() {
        return this.employerNameInput.getAttribute('value');
    };

    setEmploymentStartDateInput = function(employmentStartDate) {
        this.employmentStartDateInput.sendKeys(employmentStartDate);
    };

    getEmploymentStartDateInput = function() {
        return this.employmentStartDateInput.getAttribute('value');
    };

    setEmploymentEndDateInput = function(employmentEndDate) {
        this.employmentEndDateInput.sendKeys(employmentEndDate);
    };

    getEmploymentEndDateInput = function() {
        return this.employmentEndDateInput.getAttribute('value');
    };

    setEmploymentDurationInput = function(employmentDuration) {
        this.employmentDurationInput.sendKeys(employmentDuration);
    };

    getEmploymentDurationInput = function() {
        return this.employmentDurationInput.getAttribute('value');
    };

    getIsCurrentEmploymentInput = function() {
        return this.isCurrentEmploymentInput;
    };
    setJobDescriptionInput = function(jobDescription) {
        this.jobDescriptionInput.sendKeys(jobDescription);
    };

    getJobDescriptionInput = function() {
        return this.jobDescriptionInput.getAttribute('value');
    };

    candidateSelectLastOption = function() {
        this.candidateSelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    candidateSelectOption = function(option) {
        this.candidateSelect.sendKeys(option);
    };

    getCandidateSelect = function() {
        return this.candidateSelect;
    };

    getCandidateSelectedOption = function() {
        return this.candidateSelect.element(by.css('option:checked')).getText();
    };

    employmentTypeSelectLastOption = function() {
        this.employmentTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    employmentTypeSelectOption = function(option) {
        this.employmentTypeSelect.sendKeys(option);
    };

    getEmploymentTypeSelect = function() {
        return this.employmentTypeSelect;
    };

    getEmploymentTypeSelectedOption = function() {
        return this.employmentTypeSelect.element(by.css('option:checked')).getText();
    };

    countrySelectLastOption = function() {
        this.countrySelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    countrySelectOption = function(option) {
        this.countrySelect.sendKeys(option);
    };

    getCountrySelect = function() {
        return this.countrySelect;
    };

    getCountrySelectedOption = function() {
        return this.countrySelect.element(by.css('option:checked')).getText();
    };

    jobTypeSelectLastOption = function() {
        this.jobTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    jobTypeSelectOption = function(option) {
        this.jobTypeSelect.sendKeys(option);
    };

    getJobTypeSelect = function() {
        return this.jobTypeSelect;
    };

    getJobTypeSelectedOption = function() {
        return this.jobTypeSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
