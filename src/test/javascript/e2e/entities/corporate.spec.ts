import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Corporate e2e test', () => {

    let navBarPage: NavBarPage;
    let corporateDialogPage: CorporateDialogPage;
    let corporateComponentsPage: CorporateComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Corporates', () => {
        navBarPage.goToEntity('corporate');
        corporateComponentsPage = new CorporateComponentsPage();
        expect(corporateComponentsPage.getTitle()).toMatch(/gradzcircleApp.corporate.home.title/);

    });

    it('should load create Corporate dialog', () => {
        corporateComponentsPage.clickOnCreateButton();
        corporateDialogPage = new CorporateDialogPage();
        expect(corporateDialogPage.getModalTitle()).toMatch(/gradzcircleApp.corporate.home.createOrEditLabel/);
        corporateDialogPage.close();
    });

    it('should create and save Corporates', () => {
        corporateComponentsPage.clickOnCreateButton();
        corporateDialogPage.setCorporateNameInput('corporateName');
        expect(corporateDialogPage.getCorporateNameInput()).toMatch('corporateName');
        corporateDialogPage.setCorporateAddressInput('corporateAddress');
        expect(corporateDialogPage.getCorporateAddressInput()).toMatch('corporateAddress');
        corporateDialogPage.setCorporateCityInput('corporateCity');
        expect(corporateDialogPage.getCorporateCityInput()).toMatch('corporateCity');
        corporateDialogPage.setEstablishedSinceInput('2000-12-31');
        expect(corporateDialogPage.getEstablishedSinceInput()).toMatch('2000-12-31');
        corporateDialogPage.setCorporateEmailInput('corporateEmail');
        expect(corporateDialogPage.getCorporateEmailInput()).toMatch('corporateEmail');
        corporateDialogPage.setCorporateOverviewInput('corporateOverview');
        expect(corporateDialogPage.getCorporateOverviewInput()).toMatch('corporateOverview');
        corporateDialogPage.setCorporateBenefitsInput('corporateBenefits');
        expect(corporateDialogPage.getCorporateBenefitsInput()).toMatch('corporateBenefits');
        corporateDialogPage.setCorporateWebsiteInput('corporateWebsite');
        expect(corporateDialogPage.getCorporateWebsiteInput()).toMatch('corporateWebsite');
        corporateDialogPage.setCorporateFacebookInput('corporateFacebook');
        expect(corporateDialogPage.getCorporateFacebookInput()).toMatch('corporateFacebook');
        corporateDialogPage.setCorporateTwitterInput('corporateTwitter');
        expect(corporateDialogPage.getCorporateTwitterInput()).toMatch('corporateTwitter');
        corporateDialogPage.setCorporateInstagramInput('corporateInstagram');
        expect(corporateDialogPage.getCorporateInstagramInput()).toMatch('corporateInstagram');
        corporateDialogPage.setCorporateLinkedInInput('corporateLinkedIn');
        expect(corporateDialogPage.getCorporateLinkedInInput()).toMatch('corporateLinkedIn');
        corporateDialogPage.setCorporateCultureInput('corporateCulture');
        expect(corporateDialogPage.getCorporateCultureInput()).toMatch('corporateCulture');
        corporateDialogPage.setContactPersonInput('contactPerson');
        expect(corporateDialogPage.getContactPersonInput()).toMatch('contactPerson');
        corporateDialogPage.setCorporatePhoneInput('corporatePhone');
        expect(corporateDialogPage.getCorporatePhoneInput()).toMatch('corporatePhone');
        corporateDialogPage.setCorporatePhoneCodeInput('corporatePhoneCode');
        expect(corporateDialogPage.getCorporatePhoneCodeInput()).toMatch('corporatePhoneCode');
        corporateDialogPage.setContactPersonDesignationInput('contactPersonDesignation');
        expect(corporateDialogPage.getContactPersonDesignationInput()).toMatch('contactPersonDesignation');
        corporateDialogPage.setCorporateTagLineInput('corporateTagLine');
        expect(corporateDialogPage.getCorporateTagLineInput()).toMatch('corporateTagLine');
        corporateDialogPage.countrySelectLastOption();
        corporateDialogPage.industrySelectLastOption();
        corporateDialogPage.loginSelectLastOption();
        corporateDialogPage.save();
        expect(corporateDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CorporateComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-corporate div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CorporateDialogPage {
    modalTitle = element(by.css('h4#myCorporateLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    corporateNameInput = element(by.css('input#field_corporateName'));
    corporateAddressInput = element(by.css('input#field_corporateAddress'));
    corporateCityInput = element(by.css('input#field_corporateCity'));
    establishedSinceInput = element(by.css('input#field_establishedSince'));
    corporateEmailInput = element(by.css('input#field_corporateEmail'));
    corporateOverviewInput = element(by.css('input#field_corporateOverview'));
    corporateBenefitsInput = element(by.css('input#field_corporateBenefits'));
    corporateWebsiteInput = element(by.css('input#field_corporateWebsite'));
    corporateFacebookInput = element(by.css('input#field_corporateFacebook'));
    corporateTwitterInput = element(by.css('input#field_corporateTwitter'));
    corporateInstagramInput = element(by.css('input#field_corporateInstagram'));
    corporateLinkedInInput = element(by.css('input#field_corporateLinkedIn'));
    corporateCultureInput = element(by.css('input#field_corporateCulture'));
    contactPersonInput = element(by.css('input#field_contactPerson'));
    corporatePhoneInput = element(by.css('input#field_corporatePhone'));
    corporatePhoneCodeInput = element(by.css('input#field_corporatePhoneCode'));
    contactPersonDesignationInput = element(by.css('input#field_contactPersonDesignation'));
    corporateTagLineInput = element(by.css('input#field_corporateTagLine'));
    countrySelect = element(by.css('select#field_country'));
    industrySelect = element(by.css('select#field_industry'));
    loginSelect = element(by.css('select#field_login'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCorporateNameInput = function (corporateName) {
        this.corporateNameInput.sendKeys(corporateName);
    }

    getCorporateNameInput = function () {
        return this.corporateNameInput.getAttribute('value');
    }

    setCorporateAddressInput = function (corporateAddress) {
        this.corporateAddressInput.sendKeys(corporateAddress);
    }

    getCorporateAddressInput = function () {
        return this.corporateAddressInput.getAttribute('value');
    }

    setCorporateCityInput = function (corporateCity) {
        this.corporateCityInput.sendKeys(corporateCity);
    }

    getCorporateCityInput = function () {
        return this.corporateCityInput.getAttribute('value');
    }

    setEstablishedSinceInput = function (establishedSince) {
        this.establishedSinceInput.sendKeys(establishedSince);
    }

    getEstablishedSinceInput = function () {
        return this.establishedSinceInput.getAttribute('value');
    }

    setCorporateEmailInput = function (corporateEmail) {
        this.corporateEmailInput.sendKeys(corporateEmail);
    }

    getCorporateEmailInput = function () {
        return this.corporateEmailInput.getAttribute('value');
    }

    setCorporateOverviewInput = function (corporateOverview) {
        this.corporateOverviewInput.sendKeys(corporateOverview);
    }

    getCorporateOverviewInput = function () {
        return this.corporateOverviewInput.getAttribute('value');
    }

    setCorporateBenefitsInput = function (corporateBenefits) {
        this.corporateBenefitsInput.sendKeys(corporateBenefits);
    }

    getCorporateBenefitsInput = function () {
        return this.corporateBenefitsInput.getAttribute('value');
    }

    setCorporateWebsiteInput = function (corporateWebsite) {
        this.corporateWebsiteInput.sendKeys(corporateWebsite);
    }

    getCorporateWebsiteInput = function () {
        return this.corporateWebsiteInput.getAttribute('value');
    }

    setCorporateFacebookInput = function (corporateFacebook) {
        this.corporateFacebookInput.sendKeys(corporateFacebook);
    }

    getCorporateFacebookInput = function () {
        return this.corporateFacebookInput.getAttribute('value');
    }

    setCorporateTwitterInput = function (corporateTwitter) {
        this.corporateTwitterInput.sendKeys(corporateTwitter);
    }

    getCorporateTwitterInput = function () {
        return this.corporateTwitterInput.getAttribute('value');
    }

    setCorporateInstagramInput = function (corporateInstagram) {
        this.corporateInstagramInput.sendKeys(corporateInstagram);
    }

    getCorporateInstagramInput = function () {
        return this.corporateInstagramInput.getAttribute('value');
    }

    setCorporateLinkedInInput = function (corporateLinkedIn) {
        this.corporateLinkedInInput.sendKeys(corporateLinkedIn);
    }

    getCorporateLinkedInInput = function () {
        return this.corporateLinkedInInput.getAttribute('value');
    }

    setCorporateCultureInput = function (corporateCulture) {
        this.corporateCultureInput.sendKeys(corporateCulture);
    }

    getCorporateCultureInput = function () {
        return this.corporateCultureInput.getAttribute('value');
    }

    setContactPersonInput = function (contactPerson) {
        this.contactPersonInput.sendKeys(contactPerson);
    }

    getContactPersonInput = function () {
        return this.contactPersonInput.getAttribute('value');
    }

    setCorporatePhoneInput = function (corporatePhone) {
        this.corporatePhoneInput.sendKeys(corporatePhone);
    }

    getCorporatePhoneInput = function () {
        return this.corporatePhoneInput.getAttribute('value');
    }

    setCorporatePhoneCodeInput = function (corporatePhoneCode) {
        this.corporatePhoneCodeInput.sendKeys(corporatePhoneCode);
    }

    getCorporatePhoneCodeInput = function () {
        return this.corporatePhoneCodeInput.getAttribute('value');
    }

    setContactPersonDesignationInput = function (contactPersonDesignation) {
        this.contactPersonDesignationInput.sendKeys(contactPersonDesignation);
    }

    getContactPersonDesignationInput = function () {
        return this.contactPersonDesignationInput.getAttribute('value');
    }

    setCorporateTagLineInput = function (corporateTagLine) {
        this.corporateTagLineInput.sendKeys(corporateTagLine);
    }

    getCorporateTagLineInput = function () {
        return this.corporateTagLineInput.getAttribute('value');
    }

    countrySelectLastOption = function () {
        this.countrySelect.all(by.tagName('option')).last().click();
    }

    countrySelectOption = function (option) {
        this.countrySelect.sendKeys(option);
    }

    getCountrySelect = function () {
        return this.countrySelect;
    }

    getCountrySelectedOption = function () {
        return this.countrySelect.element(by.css('option:checked')).getText();
    }

    industrySelectLastOption = function () {
        this.industrySelect.all(by.tagName('option')).last().click();
    }

    industrySelectOption = function (option) {
        this.industrySelect.sendKeys(option);
    }

    getIndustrySelect = function () {
        return this.industrySelect;
    }

    getIndustrySelectedOption = function () {
        return this.industrySelect.element(by.css('option:checked')).getText();
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
