import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('FilterCategory e2e test', () => {

    let navBarPage: NavBarPage;
    let filterCategoryDialogPage: FilterCategoryDialogPage;
    let filterCategoryComponentsPage: FilterCategoryComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load FilterCategories', () => {
        navBarPage.goToEntity('filter-category');
        filterCategoryComponentsPage = new FilterCategoryComponentsPage();
        expect(filterCategoryComponentsPage.getTitle()).toMatch(/gradzcircleApp.filterCategory.home.title/);

    });

    it('should load create FilterCategory dialog', () => {
        filterCategoryComponentsPage.clickOnCreateButton();
        filterCategoryDialogPage = new FilterCategoryDialogPage();
        expect(filterCategoryDialogPage.getModalTitle()).toMatch(/gradzcircleApp.filterCategory.home.createOrEditLabel/);
        filterCategoryDialogPage.close();
    });

    it('should create and save FilterCategories', () => {
        filterCategoryComponentsPage.clickOnCreateButton();
        filterCategoryDialogPage.setFilterCategoryInput('filterCategory');
        expect(filterCategoryDialogPage.getFilterCategoryInput()).toMatch('filterCategory');
        filterCategoryDialogPage.setFilterCostInput('5');
        expect(filterCategoryDialogPage.getFilterCostInput()).toMatch('5');
        filterCategoryDialogPage.save();
        expect(filterCategoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FilterCategoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-filter-category div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FilterCategoryDialogPage {
    modalTitle = element(by.css('h4#myFilterCategoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    filterCategoryInput = element(by.css('input#field_filterCategory'));
    filterCostInput = element(by.css('input#field_filterCost'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFilterCategoryInput = function (filterCategory) {
        this.filterCategoryInput.sendKeys(filterCategory);
    }

    getFilterCategoryInput = function () {
        return this.filterCategoryInput.getAttribute('value');
    }

    setFilterCostInput = function (filterCost) {
        this.filterCostInput.sendKeys(filterCost);
    }

    getFilterCostInput = function () {
        return this.filterCostInput.getAttribute('value');
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
