import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('EmploymentType e2e test', () => {

    let navBarPage: NavBarPage;
    let employmentTypeDialogPage: EmploymentTypeDialogPage;
    let employmentTypeComponentsPage: EmploymentTypeComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load EmploymentTypes', () => {
        navBarPage.goToEntity('employment-type');
        employmentTypeComponentsPage = new EmploymentTypeComponentsPage();
        expect(employmentTypeComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.employmentType.home.title/);

    });

    it('should load create EmploymentType dialog', () => {
        employmentTypeComponentsPage.clickOnCreateButton();
        employmentTypeDialogPage = new EmploymentTypeDialogPage();
        expect(employmentTypeDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.employmentType.home.createOrEditLabel/);
        employmentTypeDialogPage.close();
    });

    it('should create and save EmploymentTypes', () => {
        employmentTypeComponentsPage.clickOnCreateButton();
        employmentTypeDialogPage.setEmploymentTypeInput('employmentType');
        expect(employmentTypeDialogPage.getEmploymentTypeInput()).toMatch('employmentType');
        employmentTypeDialogPage.setEmploymentTypeCostInput('5');
        expect(employmentTypeDialogPage.getEmploymentTypeCostInput()).toMatch('5');
        employmentTypeDialogPage.save();
        expect(employmentTypeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EmploymentTypeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-employment-type div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmploymentTypeDialogPage {
    modalTitle = element(by.css('h4#myEmploymentTypeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    employmentTypeInput = element(by.css('input#field_employmentType'));
    employmentTypeCostInput = element(by.css('input#field_employmentTypeCost'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setEmploymentTypeInput = function(employmentType) {
        this.employmentTypeInput.sendKeys(employmentType);
    };

    getEmploymentTypeInput = function() {
        return this.employmentTypeInput.getAttribute('value');
    };

    setEmploymentTypeCostInput = function(employmentTypeCost) {
        this.employmentTypeCostInput.sendKeys(employmentTypeCost);
    };

    getEmploymentTypeCostInput = function() {
        return this.employmentTypeCostInput.getAttribute('value');
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
