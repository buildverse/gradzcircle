import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('ProfileCategory e2e test', () => {

    let navBarPage: NavBarPage;
    let profileCategoryDialogPage: ProfileCategoryDialogPage;
    let profileCategoryComponentsPage: ProfileCategoryComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ProfileCategories', () => {
        navBarPage.goToEntity('profile-category');
        profileCategoryComponentsPage = new ProfileCategoryComponentsPage();
        expect(profileCategoryComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.profileCategory.home.title/);

    });

    it('should load create ProfileCategory dialog', () => {
        profileCategoryComponentsPage.clickOnCreateButton();
        profileCategoryDialogPage = new ProfileCategoryDialogPage();
        expect(profileCategoryDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.profileCategory.home.createOrEditLabel/);
        profileCategoryDialogPage.close();
    });

    it('should create and save ProfileCategories', () => {
        profileCategoryComponentsPage.clickOnCreateButton();
        profileCategoryDialogPage.setCategoryNameInput('categoryName');
        expect(profileCategoryDialogPage.getCategoryNameInput()).toMatch('categoryName');
        profileCategoryDialogPage.setWeightageInput('5');
        expect(profileCategoryDialogPage.getWeightageInput()).toMatch('5');
        profileCategoryDialogPage.save();
        expect(profileCategoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProfileCategoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-profile-category div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ProfileCategoryDialogPage {
    modalTitle = element(by.css('h4#myProfileCategoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    categoryNameInput = element(by.css('input#field_categoryName'));
    weightageInput = element(by.css('input#field_weightage'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCategoryNameInput = function(categoryName) {
        this.categoryNameInput.sendKeys(categoryName);
    };

    getCategoryNameInput = function() {
        return this.categoryNameInput.getAttribute('value');
    };

    setWeightageInput = function(weightage) {
        this.weightageInput.sendKeys(weightage);
    };

    getWeightageInput = function() {
        return this.weightageInput.getAttribute('value');
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
