import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Audit e2e test', () => {

    let navBarPage: NavBarPage;
    let auditDialogPage: AuditDialogPage;
    let auditComponentsPage: AuditComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Audits', () => {
        navBarPage.goToEntity('audit');
        auditComponentsPage = new AuditComponentsPage();
        expect(auditComponentsPage.getTitle()).toMatch(/gradzcircleApp.audit.home.title/);

    });

    it('should load create Audit dialog', () => {
        auditComponentsPage.clickOnCreateButton();
        auditDialogPage = new AuditDialogPage();
        expect(auditDialogPage.getModalTitle()).toMatch(/gradzcircleApp.audit.home.createOrEditLabel/);
        auditDialogPage.close();
    });

    it('should create and save Audits', () => {
        auditComponentsPage.clickOnCreateButton();
        auditDialogPage.setCreatedByInput('5');
        expect(auditDialogPage.getCreatedByInput()).toMatch('5');
        auditDialogPage.setUpdatedByInput('5');
        expect(auditDialogPage.getUpdatedByInput()).toMatch('5');
        auditDialogPage.setCreatedTimeInput(12310020012301);
        expect(auditDialogPage.getCreatedTimeInput()).toMatch('2001-12-31T02:30');
        auditDialogPage.setUpdatedTimeInput(12310020012301);
        expect(auditDialogPage.getUpdatedTimeInput()).toMatch('2001-12-31T02:30');
        auditDialogPage.save();
        expect(auditDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AuditComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-audit div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AuditDialogPage {
    modalTitle = element(by.css('h4#myAuditLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    createdByInput = element(by.css('input#field_createdBy'));
    updatedByInput = element(by.css('input#field_updatedBy'));
    createdTimeInput = element(by.css('input#field_createdTime'));
    updatedTimeInput = element(by.css('input#field_updatedTime'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCreatedByInput = function (createdBy) {
        this.createdByInput.sendKeys(createdBy);
    }

    getCreatedByInput = function () {
        return this.createdByInput.getAttribute('value');
    }

    setUpdatedByInput = function (updatedBy) {
        this.updatedByInput.sendKeys(updatedBy);
    }

    getUpdatedByInput = function () {
        return this.updatedByInput.getAttribute('value');
    }

    setCreatedTimeInput = function (createdTime) {
        this.createdTimeInput.sendKeys(createdTime);
    }

    getCreatedTimeInput = function () {
        return this.createdTimeInput.getAttribute('value');
    }

    setUpdatedTimeInput = function (updatedTime) {
        this.updatedTimeInput.sendKeys(updatedTime);
    }

    getUpdatedTimeInput = function () {
        return this.updatedTimeInput.getAttribute('value');
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
