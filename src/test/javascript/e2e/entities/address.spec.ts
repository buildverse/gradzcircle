import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Address e2e test', () => {

    let navBarPage: NavBarPage;
    let addressDialogPage: AddressDialogPage;
    let addressComponentsPage: AddressComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Addresses', () => {
        navBarPage.goToEntity('address');
        addressComponentsPage = new AddressComponentsPage();
        expect(addressComponentsPage.getTitle()).toMatch(/gradzcircleApp.address.home.title/);

    });

    it('should load create Address dialog', () => {
        addressComponentsPage.clickOnCreateButton();
        addressDialogPage = new AddressDialogPage();
        expect(addressDialogPage.getModalTitle()).toMatch(/gradzcircleApp.address.home.createOrEditLabel/);
        addressDialogPage.close();
    });

    it('should create and save Addresses', () => {
        addressComponentsPage.clickOnCreateButton();
        addressDialogPage.setAddressLineOneInput('addressLineOne');
        expect(addressDialogPage.getAddressLineOneInput()).toMatch('addressLineOne');
        addressDialogPage.setAddressLineTwoInput('addressLineTwo');
        expect(addressDialogPage.getAddressLineTwoInput()).toMatch('addressLineTwo');
        addressDialogPage.setCityInput('city');
        expect(addressDialogPage.getCityInput()).toMatch('city');
        addressDialogPage.setStateInput('state');
        expect(addressDialogPage.getStateInput()).toMatch('state');
        addressDialogPage.setZipInput('zip');
        expect(addressDialogPage.getZipInput()).toMatch('zip');
        addressDialogPage.candidateSelectLastOption();
        addressDialogPage.countrySelectLastOption();
        addressDialogPage.save();
        expect(addressDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AddressComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-address div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AddressDialogPage {
    modalTitle = element(by.css('h4#myAddressLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    addressLineOneInput = element(by.css('input#field_addressLineOne'));
    addressLineTwoInput = element(by.css('input#field_addressLineTwo'));
    cityInput = element(by.css('input#field_city'));
    stateInput = element(by.css('input#field_state'));
    zipInput = element(by.css('input#field_zip'));
    candidateSelect = element(by.css('select#field_candidate'));
    countrySelect = element(by.css('select#field_country'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setAddressLineOneInput = function (addressLineOne) {
        this.addressLineOneInput.sendKeys(addressLineOne);
    }

    getAddressLineOneInput = function () {
        return this.addressLineOneInput.getAttribute('value');
    }

    setAddressLineTwoInput = function (addressLineTwo) {
        this.addressLineTwoInput.sendKeys(addressLineTwo);
    }

    getAddressLineTwoInput = function () {
        return this.addressLineTwoInput.getAttribute('value');
    }

    setCityInput = function (city) {
        this.cityInput.sendKeys(city);
    }

    getCityInput = function () {
        return this.cityInput.getAttribute('value');
    }

    setStateInput = function (state) {
        this.stateInput.sendKeys(state);
    }

    getStateInput = function () {
        return this.stateInput.getAttribute('value');
    }

    setZipInput = function (zip) {
        this.zipInput.sendKeys(zip);
    }

    getZipInput = function () {
        return this.zipInput.getAttribute('value');
    }

    candidateSelectLastOption = function () {
        this.candidateSelect.all(by.tagName('option')).last().click();
    }

    candidateSelectOption = function (option) {
        this.candidateSelect.sendKeys(option);
    }

    getCandidateSelect = function () {
        return this.candidateSelect;
    }

    getCandidateSelectedOption = function () {
        return this.candidateSelect.element(by.css('option:checked')).getText();
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
