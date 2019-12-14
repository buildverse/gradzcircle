import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('AppConfig e2e test', () => {
    let navBarPage: NavBarPage;
    let appConfigDialogPage: AppConfigDialogPage;
    let appConfigComponentsPage: AppConfigComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load AppConfigs', () => {
        navBarPage.goToEntity('app-config');
        appConfigComponentsPage = new AppConfigComponentsPage();
        expect(appConfigComponentsPage.getTitle()).toMatch(/gradzcircleApp.appConfig.home.title/);
    });

    it('should load create AppConfig dialog', () => {
        appConfigComponentsPage.clickOnCreateButton();
        appConfigDialogPage = new AppConfigDialogPage();
        expect(appConfigDialogPage.getModalTitle()).toMatch(/gradzcircleApp.appConfig.home.createOrEditLabel/);
        appConfigDialogPage.close();
    });

    it('should create and save AppConfigs', () => {
        appConfigComponentsPage.clickOnCreateButton();
        appConfigDialogPage.setConfigNameInput('configName');
        expect(appConfigDialogPage.getConfigNameInput()).toMatch('configName');
        appConfigDialogPage
            .getConfigValueInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    appConfigDialogPage.getConfigValueInput().click();
                    expect(appConfigDialogPage.getConfigValueInput().isSelected()).toBeFalsy();
                } else {
                    appConfigDialogPage.getConfigValueInput().click();
                    expect(appConfigDialogPage.getConfigValueInput().isSelected()).toBeTruthy();
                }
            });
        appConfigDialogPage.save();
        expect(appConfigDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AppConfigComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-app-config div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AppConfigDialogPage {
    modalTitle = element(by.css('h4#myAppConfigLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    configNameInput = element(by.css('input#field_configName'));
    configValueInput = element(by.css('input#field_configValue'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setConfigNameInput = function(configName) {
        this.configNameInput.sendKeys(configName);
    };

    getConfigNameInput = function() {
        return this.configNameInput.getAttribute('value');
    };

    getConfigValueInput = function() {
        return this.configValueInput;
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
