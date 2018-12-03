import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('States e2e test', () => {

    let navBarPage: NavBarPage;
    let statesDialogPage: StatesDialogPage;
    let statesComponentsPage: StatesComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load States', () => {
        navBarPage.goToEntity('states');
        statesComponentsPage = new StatesComponentsPage();
        expect(statesComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.states.home.title/);

    });

    it('should load create States dialog', () => {
        statesComponentsPage.clickOnCreateButton();
        statesDialogPage = new StatesDialogPage();
        expect(statesDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.states.home.createOrEditLabel/);
        statesDialogPage.close();
    });

    it('should create and save States', () => {
        statesComponentsPage.clickOnCreateButton();
        statesDialogPage.setNameInput('name');
        expect(statesDialogPage.getNameInput()).toMatch('name');
        statesDialogPage.save();
        expect(statesDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class StatesComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-states div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class StatesDialogPage {
    modalTitle = element(by.css('h4#myStatesLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    };

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
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
