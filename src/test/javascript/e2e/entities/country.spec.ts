import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Country e2e test', () => {
    let navBarPage: NavBarPage;
    let countryDialogPage: CountryDialogPage;
    let countryComponentsPage: CountryComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Countries', () => {
        navBarPage.goToEntity('country');
        countryComponentsPage = new CountryComponentsPage();
        expect(countryComponentsPage.getTitle()).toMatch(/gradzcircleApp.country.home.title/);
    });

    it('should load create Country dialog', () => {
        countryComponentsPage.clickOnCreateButton();
        countryDialogPage = new CountryDialogPage();
        expect(countryDialogPage.getModalTitle()).toMatch(/gradzcircleApp.country.home.createOrEditLabel/);
        countryDialogPage.close();
    });

    it('should create and save Countries', () => {
        countryComponentsPage.clickOnCreateButton();
        countryDialogPage.setCountryNameInput('countryName');
        expect(countryDialogPage.getCountryNameInput()).toMatch('countryName');
        countryDialogPage.setShortCodeInput('shortCode');
        expect(countryDialogPage.getShortCodeInput()).toMatch('shortCode');
        countryDialogPage.setShortCodeThreeCharInput('shortCodeThreeChar');
        expect(countryDialogPage.getShortCodeThreeCharInput()).toMatch('shortCodeThreeChar');
        countryDialogPage.setCountryNiceNameInput('countryNiceName');
        expect(countryDialogPage.getCountryNiceNameInput()).toMatch('countryNiceName');
        countryDialogPage.setNumCodeInput('5');
        expect(countryDialogPage.getNumCodeInput()).toMatch('5');
        countryDialogPage.setPhoneCodeInput('5');
        expect(countryDialogPage.getPhoneCodeInput()).toMatch('5');
        countryDialogPage
            .getEnabledInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    countryDialogPage.getEnabledInput().click();
                    expect(countryDialogPage.getEnabledInput().isSelected()).toBeFalsy();
                } else {
                    countryDialogPage.getEnabledInput().click();
                    expect(countryDialogPage.getEnabledInput().isSelected()).toBeTruthy();
                }
            });
        countryDialogPage.nationalitySelectLastOption();
        countryDialogPage.save();
        expect(countryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CountryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-country div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CountryDialogPage {
    modalTitle = element(by.css('h4#myCountryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    countryNameInput = element(by.css('input#field_countryName'));
    shortCodeInput = element(by.css('input#field_shortCode'));
    shortCodeThreeCharInput = element(by.css('input#field_shortCodeThreeChar'));
    countryNiceNameInput = element(by.css('input#field_countryNiceName'));
    numCodeInput = element(by.css('input#field_numCode'));
    phoneCodeInput = element(by.css('input#field_phoneCode'));
    enabledInput = element(by.css('input#field_enabled'));
    nationalitySelect = element(by.css('select#field_nationality'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCountryNameInput = function(countryName) {
        this.countryNameInput.sendKeys(countryName);
    };

    getCountryNameInput = function() {
        return this.countryNameInput.getAttribute('value');
    };

    setShortCodeInput = function(shortCode) {
        this.shortCodeInput.sendKeys(shortCode);
    };

    getShortCodeInput = function() {
        return this.shortCodeInput.getAttribute('value');
    };

    setShortCodeThreeCharInput = function(shortCodeThreeChar) {
        this.shortCodeThreeCharInput.sendKeys(shortCodeThreeChar);
    };

    getShortCodeThreeCharInput = function() {
        return this.shortCodeThreeCharInput.getAttribute('value');
    };

    setCountryNiceNameInput = function(countryNiceName) {
        this.countryNiceNameInput.sendKeys(countryNiceName);
    };

    getCountryNiceNameInput = function() {
        return this.countryNiceNameInput.getAttribute('value');
    };

    setNumCodeInput = function(numCode) {
        this.numCodeInput.sendKeys(numCode);
    };

    getNumCodeInput = function() {
        return this.numCodeInput.getAttribute('value');
    };

    setPhoneCodeInput = function(phoneCode) {
        this.phoneCodeInput.sendKeys(phoneCode);
    };

    getPhoneCodeInput = function() {
        return this.phoneCodeInput.getAttribute('value');
    };

    getEnabledInput = function() {
        return this.enabledInput;
    };
    nationalitySelectLastOption = function() {
        this.nationalitySelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    nationalitySelectOption = function(option) {
        this.nationalitySelect.sendKeys(option);
    };

    getNationalitySelect = function() {
        return this.nationalitySelect;
    };

    getNationalitySelectedOption = function() {
        return this.nationalitySelect.element(by.css('option:checked')).getText();
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
