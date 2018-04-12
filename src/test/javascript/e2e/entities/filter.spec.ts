import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Filter e2e test', () => {

    let navBarPage: NavBarPage;
    let filterDialogPage: FilterDialogPage;
    let filterComponentsPage: FilterComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Filters', () => {
        navBarPage.goToEntity('filter');
        filterComponentsPage = new FilterComponentsPage();
        expect(filterComponentsPage.getTitle()).toMatch(/gradzcircleApp.filter.home.title/);

    });

    it('should load create Filter dialog', () => {
        filterComponentsPage.clickOnCreateButton();
        filterDialogPage = new FilterDialogPage();
        expect(filterDialogPage.getModalTitle()).toMatch(/gradzcircleApp.filter.home.createOrEditLabel/);
        filterDialogPage.close();
    });

    it('should create and save Filters', () => {
        filterComponentsPage.clickOnCreateButton();
        filterDialogPage.setFilterNameInput('filterName');
        expect(filterDialogPage.getFilterNameInput()).toMatch('filterName');
        filterDialogPage.setFilterCostInput('5');
        expect(filterDialogPage.getFilterCostInput()).toMatch('5');
        filterDialogPage.setCommentsInput('comments');
        expect(filterDialogPage.getCommentsInput()).toMatch('comments');
        filterDialogPage.save();
        expect(filterDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FilterComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-filter div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FilterDialogPage {
    modalTitle = element(by.css('h4#myFilterLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    filterNameInput = element(by.css('input#field_filterName'));
    filterCostInput = element(by.css('input#field_filterCost'));
    commentsInput = element(by.css('input#field_comments'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFilterNameInput = function (filterName) {
        this.filterNameInput.sendKeys(filterName);
    }

    getFilterNameInput = function () {
        return this.filterNameInput.getAttribute('value');
    }

    setFilterCostInput = function (filterCost) {
        this.filterCostInput.sendKeys(filterCost);
    }

    getFilterCostInput = function () {
        return this.filterCostInput.getAttribute('value');
    }

    setCommentsInput = function (comments) {
        this.commentsInput.sendKeys(comments);
    }

    getCommentsInput = function () {
        return this.commentsInput.getAttribute('value');
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
