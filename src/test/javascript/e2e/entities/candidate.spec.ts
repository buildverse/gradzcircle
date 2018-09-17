import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Candidate e2e test', () => {

    let navBarPage: NavBarPage;
    let candidateDialogPage: CandidateDialogPage;
    let candidateComponentsPage: CandidateComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Candidates', () => {
        navBarPage.goToEntity('candidate');
        candidateComponentsPage = new CandidateComponentsPage();
        expect(candidateComponentsPage.getTitle()).toMatch(/gradzcircleApp.candidate.home.title/);

    });

    it('should load create Candidate dialog', () => {
        candidateComponentsPage.clickOnCreateButton();
        candidateDialogPage = new CandidateDialogPage();
        expect(candidateDialogPage.getModalTitle()).toMatch(/gradzcircleApp.candidate.home.createOrEditLabel/);
        candidateDialogPage.close();
    });

    it('should create and save Candidates', () => {
        candidateComponentsPage.clickOnCreateButton();
        candidateDialogPage.setFirstNameInput('firstName');
        expect(candidateDialogPage.getFirstNameInput()).toMatch('firstName');
        candidateDialogPage.setLastNameInput('lastName');
        expect(candidateDialogPage.getLastNameInput()).toMatch('lastName');
        candidateDialogPage.setMiddleNameInput('middleName');
        expect(candidateDialogPage.getMiddleNameInput()).toMatch('middleName');
        candidateDialogPage.setFacebookInput('facebook');
        expect(candidateDialogPage.getFacebookInput()).toMatch('facebook');
        candidateDialogPage.setLinkedInInput('linkedIn');
        expect(candidateDialogPage.getLinkedInInput()).toMatch('linkedIn');
        candidateDialogPage.setTwitterInput('twitter');
        expect(candidateDialogPage.getTwitterInput()).toMatch('twitter');
        candidateDialogPage.setAboutMeInput('aboutMe');
        expect(candidateDialogPage.getAboutMeInput()).toMatch('aboutMe');
        candidateDialogPage.setDateOfBirthInput('2000-12-31');
        expect(candidateDialogPage.getDateOfBirthInput()).toMatch('2000-12-31');
        candidateDialogPage.setPhoneCodeInput('phoneCode');
        expect(candidateDialogPage.getPhoneCodeInput()).toMatch('phoneCode');
        candidateDialogPage.setPhoneNumberInput('phoneNumber');
        expect(candidateDialogPage.getPhoneNumberInput()).toMatch('phoneNumber');
        candidateDialogPage.getDifferentlyAbledInput().isSelected().then(function (selected) {
            if (selected) {
                candidateDialogPage.getDifferentlyAbledInput().click();
                expect(candidateDialogPage.getDifferentlyAbledInput().isSelected()).toBeFalsy();
            } else {
                candidateDialogPage.getDifferentlyAbledInput().click();
                expect(candidateDialogPage.getDifferentlyAbledInput().isSelected()).toBeTruthy();
            }
        });
        candidateDialogPage.getAvailableForHiringInput().isSelected().then(function (selected) {
            if (selected) {
                candidateDialogPage.getAvailableForHiringInput().click();
                expect(candidateDialogPage.getAvailableForHiringInput().isSelected()).toBeFalsy();
            } else {
                candidateDialogPage.getAvailableForHiringInput().click();
                expect(candidateDialogPage.getAvailableForHiringInput().isSelected()).toBeTruthy();
            }
        });
        candidateDialogPage.getOpenToRelocateInput().isSelected().then(function (selected) {
            if (selected) {
                candidateDialogPage.getOpenToRelocateInput().click();
                expect(candidateDialogPage.getOpenToRelocateInput().isSelected()).toBeFalsy();
            } else {
                candidateDialogPage.getOpenToRelocateInput().click();
                expect(candidateDialogPage.getOpenToRelocateInput().isSelected()).toBeTruthy();
            }
        });
        candidateDialogPage.getMatchEligibleInput().isSelected().then(function (selected) {
            if (selected) {
                candidateDialogPage.getMatchEligibleInput().click();
                expect(candidateDialogPage.getMatchEligibleInput().isSelected()).toBeFalsy();
            } else {
                candidateDialogPage.getMatchEligibleInput().click();
                expect(candidateDialogPage.getMatchEligibleInput().isSelected()).toBeTruthy();
            }
        });
        candidateDialogPage.loginSelectLastOption();
        candidateDialogPage.nationalitySelectLastOption();
        candidateDialogPage.genderSelectLastOption();
        candidateDialogPage.maritalStatusSelectLastOption();
        // candidateDialogPage.jobCategorySelectLastOption();
        // candidateDialogPage.jobSelectLastOption();
        candidateDialogPage.visaTypeSelectLastOption();
        candidateDialogPage.save();
        expect(candidateDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateDialogPage {
    modalTitle = element(by.css('h4#myCandidateLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    firstNameInput = element(by.css('input#field_firstName'));
    lastNameInput = element(by.css('input#field_lastName'));
    middleNameInput = element(by.css('input#field_middleName'));
    facebookInput = element(by.css('input#field_facebook'));
    linkedInInput = element(by.css('input#field_linkedIn'));
    twitterInput = element(by.css('input#field_twitter'));
    aboutMeInput = element(by.css('input#field_aboutMe'));
    dateOfBirthInput = element(by.css('input#field_dateOfBirth'));
    phoneCodeInput = element(by.css('input#field_phoneCode'));
    phoneNumberInput = element(by.css('input#field_phoneNumber'));
    differentlyAbledInput = element(by.css('input#field_differentlyAbled'));
    availableForHiringInput = element(by.css('input#field_availableForHiring'));
    openToRelocateInput = element(by.css('input#field_openToRelocate'));
    matchEligibleInput = element(by.css('input#field_matchEligible'));
    loginSelect = element(by.css('select#field_login'));
    nationalitySelect = element(by.css('select#field_nationality'));
    genderSelect = element(by.css('select#field_gender'));
    maritalStatusSelect = element(by.css('select#field_maritalStatus'));
    jobCategorySelect = element(by.css('select#field_jobCategory'));
    jobSelect = element(by.css('select#field_job'));
    visaTypeSelect = element(by.css('select#field_visaType'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFirstNameInput = function (firstName) {
        this.firstNameInput.sendKeys(firstName);
    }

    getFirstNameInput = function () {
        return this.firstNameInput.getAttribute('value');
    }

    setLastNameInput = function (lastName) {
        this.lastNameInput.sendKeys(lastName);
    }

    getLastNameInput = function () {
        return this.lastNameInput.getAttribute('value');
    }

    setMiddleNameInput = function (middleName) {
        this.middleNameInput.sendKeys(middleName);
    }

    getMiddleNameInput = function () {
        return this.middleNameInput.getAttribute('value');
    }

    setFacebookInput = function (facebook) {
        this.facebookInput.sendKeys(facebook);
    }

    getFacebookInput = function () {
        return this.facebookInput.getAttribute('value');
    }

    setLinkedInInput = function (linkedIn) {
        this.linkedInInput.sendKeys(linkedIn);
    }

    getLinkedInInput = function () {
        return this.linkedInInput.getAttribute('value');
    }

    setTwitterInput = function (twitter) {
        this.twitterInput.sendKeys(twitter);
    }

    getTwitterInput = function () {
        return this.twitterInput.getAttribute('value');
    }

    setAboutMeInput = function (aboutMe) {
        this.aboutMeInput.sendKeys(aboutMe);
    }

    getAboutMeInput = function () {
        return this.aboutMeInput.getAttribute('value');
    }

    setDateOfBirthInput = function (dateOfBirth) {
        this.dateOfBirthInput.sendKeys(dateOfBirth);
    }

    getDateOfBirthInput = function () {
        return this.dateOfBirthInput.getAttribute('value');
    }

    setPhoneCodeInput = function (phoneCode) {
        this.phoneCodeInput.sendKeys(phoneCode);
    }

    getPhoneCodeInput = function () {
        return this.phoneCodeInput.getAttribute('value');
    }

    setPhoneNumberInput = function (phoneNumber) {
        this.phoneNumberInput.sendKeys(phoneNumber);
    }

    getPhoneNumberInput = function () {
        return this.phoneNumberInput.getAttribute('value');
    }

    getDifferentlyAbledInput = function () {
        return this.differentlyAbledInput;
    }
    getAvailableForHiringInput = function () {
        return this.availableForHiringInput;
    }
    getOpenToRelocateInput = function () {
        return this.openToRelocateInput;
    }
    getMatchEligibleInput = function () {
        return this.matchEligibleInput;
    }
    loginSelectLastOption = function () {
        this.loginSelect.all(by.tagName('option')).last().click();
    }

    loginSelectOption = function (option) {
        this.loginSelect.sendKeys(option);
    }

    getLoginSelect = function () {
        return this.loginSelect;
    }

    getLoginSelectedOption = function () {
        return this.loginSelect.element(by.css('option:checked')).getText();
    }

    nationalitySelectLastOption = function () {
        this.nationalitySelect.all(by.tagName('option')).last().click();
    }

    nationalitySelectOption = function (option) {
        this.nationalitySelect.sendKeys(option);
    }

    getNationalitySelect = function () {
        return this.nationalitySelect;
    }

    getNationalitySelectedOption = function () {
        return this.nationalitySelect.element(by.css('option:checked')).getText();
    }

    genderSelectLastOption = function () {
        this.genderSelect.all(by.tagName('option')).last().click();
    }

    genderSelectOption = function (option) {
        this.genderSelect.sendKeys(option);
    }

    getGenderSelect = function () {
        return this.genderSelect;
    }

    getGenderSelectedOption = function () {
        return this.genderSelect.element(by.css('option:checked')).getText();
    }

    maritalStatusSelectLastOption = function () {
        this.maritalStatusSelect.all(by.tagName('option')).last().click();
    }

    maritalStatusSelectOption = function (option) {
        this.maritalStatusSelect.sendKeys(option);
    }

    getMaritalStatusSelect = function () {
        return this.maritalStatusSelect;
    }

    getMaritalStatusSelectedOption = function () {
        return this.maritalStatusSelect.element(by.css('option:checked')).getText();
    }

    jobCategorySelectLastOption = function () {
        this.jobCategorySelect.all(by.tagName('option')).last().click();
    }

    jobCategorySelectOption = function (option) {
        this.jobCategorySelect.sendKeys(option);
    }

    getJobCategorySelect = function () {
        return this.jobCategorySelect;
    }

    getJobCategorySelectedOption = function () {
        return this.jobCategorySelect.element(by.css('option:checked')).getText();
    }

    jobSelectLastOption = function () {
        this.jobSelect.all(by.tagName('option')).last().click();
    }

    jobSelectOption = function (option) {
        this.jobSelect.sendKeys(option);
    }

    getJobSelect = function () {
        return this.jobSelect;
    }

    getJobSelectedOption = function () {
        return this.jobSelect.element(by.css('option:checked')).getText();
    }

    visaTypeSelectLastOption = function () {
        this.visaTypeSelect.all(by.tagName('option')).last().click();
    }

    visaTypeSelectOption = function (option) {
        this.visaTypeSelect.sendKeys(option);
    }

    getVisaTypeSelect = function () {
        return this.visaTypeSelect;
    }

    getVisaTypeSelectedOption = function () {
        return this.visaTypeSelect.element(by.css('option:checked')).getText();
    }

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
