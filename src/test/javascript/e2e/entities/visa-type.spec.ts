import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('VisaType e2e test', () => {

    let navBarPage: NavBarPage;
    let visaTypeDialogPage: VisaTypeDialogPage;
    let visaTypeComponentsPage: VisaTypeComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load VisaTypes', () => {
        navBarPage.goToEntity('visa-type');
        visaTypeComponentsPage = new VisaTypeComponentsPage();
        expect(visaTypeComponentsPage.getTitle()).toMatch(/gradzcircleApp.visaType.home.title/);

    });

    it('should load create VisaType dialog', () => {
        visaTypeComponentsPage.clickOnCreateButton();
        visaTypeDialogPage = new VisaTypeDialogPage();
        expect(visaTypeDialogPage.getModalTitle()).toMatch(/gradzcircleApp.visaType.home.createOrEditLabel/);
        visaTypeDialogPage.close();
    });

    it('should create and save VisaTypes', () => {
        visaTypeComponentsPage.clickOnCreateButton();
        visaTypeDialogPage.setVisaInput('visa');
        expect(visaTypeDialogPage.getVisaInput()).toMatch('visa');
        visaTypeDialogPage.countrySelectLastOption();
        visaTypeDialogPage.save();
        expect(visaTypeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class VisaTypeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-visa-type div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class VisaTypeDialogPage {
    modalTitle = element(by.css('h4#myVisaTypeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    visaInput = element(by.css('input#field_visa'));
    countrySelect = element(by.css('select#field_country'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setVisaInput = function (visa) {
        this.visaInput.sendKeys(visa);
    }

    getVisaInput = function () {
        return this.visaInput.getAttribute('value');
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
