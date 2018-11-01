import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('ErrorMessages e2e test', () => {

    let navBarPage: NavBarPage;
    let errorMessagesDialogPage: ErrorMessagesDialogPage;
    let errorMessagesComponentsPage: ErrorMessagesComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ErrorMessages', () => {
        navBarPage.goToEntity('error-messages');
        errorMessagesComponentsPage = new ErrorMessagesComponentsPage();
        expect(errorMessagesComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.errorMessages.home.title/);

    });

    it('should load create ErrorMessages dialog', () => {
        errorMessagesComponentsPage.clickOnCreateButton();
        errorMessagesDialogPage = new ErrorMessagesDialogPage();
        expect(errorMessagesDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.errorMessages.home.createOrEditLabel/);
        errorMessagesDialogPage.close();
    });

    it('should create and save ErrorMessages', () => {
        errorMessagesComponentsPage.clickOnCreateButton();
        errorMessagesDialogPage.setComponentNameInput('componentName');
        expect(errorMessagesDialogPage.getComponentNameInput()).toMatch('componentName');
        errorMessagesDialogPage.setErrorKeyInput('errorKey');
        expect(errorMessagesDialogPage.getErrorKeyInput()).toMatch('errorKey');
        errorMessagesDialogPage.setErrorMessageInput('errorMessage');
        expect(errorMessagesDialogPage.getErrorMessageInput()).toMatch('errorMessage');
        errorMessagesDialogPage.save();
        expect(errorMessagesDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ErrorMessagesComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-error-messages div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ErrorMessagesDialogPage {
    modalTitle = element(by.css('h4#myErrorMessagesLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    componentNameInput = element(by.css('input#field_componentName'));
    errorKeyInput = element(by.css('input#field_errorKey'));
    errorMessageInput = element(by.css('input#field_errorMessage'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setComponentNameInput = function(componentName) {
        this.componentNameInput.sendKeys(componentName);
    };

    getComponentNameInput = function() {
        return this.componentNameInput.getAttribute('value');
    };

    setErrorKeyInput = function(errorKey) {
        this.errorKeyInput.sendKeys(errorKey);
    };

    getErrorKeyInput = function() {
        return this.errorKeyInput.getAttribute('value');
    };

    setErrorMessageInput = function(errorMessage) {
        this.errorMessageInput.sendKeys(errorMessage);
    };

    getErrorMessageInput = function() {
        return this.errorMessageInput.getAttribute('value');
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
