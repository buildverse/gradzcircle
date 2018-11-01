import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('University e2e test', () => {

    let navBarPage: NavBarPage;
    let universityDialogPage: UniversityDialogPage;
    let universityComponentsPage: UniversityComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Universities', () => {
        navBarPage.goToEntity('university');
        universityComponentsPage = new UniversityComponentsPage();
        expect(universityComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.university.home.title/);

    });

    it('should load create University dialog', () => {
        universityComponentsPage.clickOnCreateButton();
        universityDialogPage = new UniversityDialogPage();
        expect(universityDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.university.home.createOrEditLabel/);
        universityDialogPage.close();
    });

    it('should create and save Universities', () => {
        universityComponentsPage.clickOnCreateButton();
        universityDialogPage.setUniversityNameInput('universityName');
        expect(universityDialogPage.getUniversityNameInput()).toMatch('universityName');
        universityDialogPage.setUniversityTypeInput('universityType');
        expect(universityDialogPage.getUniversityTypeInput()).toMatch('universityType');
        universityDialogPage.setWebsiteInput('website');
        expect(universityDialogPage.getWebsiteInput()).toMatch('website');
        universityDialogPage.countrySelectLastOption();
        universityDialogPage.save();
        expect(universityDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class UniversityComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-university div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class UniversityDialogPage {
    modalTitle = element(by.css('h4#myUniversityLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    universityNameInput = element(by.css('input#field_universityName'));
    universityTypeInput = element(by.css('input#field_universityType'));
    websiteInput = element(by.css('input#field_website'));
    countrySelect = element(by.css('select#field_country'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setUniversityNameInput = function(universityName) {
        this.universityNameInput.sendKeys(universityName);
    };

    getUniversityNameInput = function() {
        return this.universityNameInput.getAttribute('value');
    };

    setUniversityTypeInput = function(universityType) {
        this.universityTypeInput.sendKeys(universityType);
    };

    getUniversityTypeInput = function() {
        return this.universityTypeInput.getAttribute('value');
    };

    setWebsiteInput = function(website) {
        this.websiteInput.sendKeys(website);
    };

    getWebsiteInput = function() {
        return this.websiteInput.getAttribute('value');
    };

    countrySelectLastOption = function() {
        this.countrySelect.all(by.tagName('option')).last().click();
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
