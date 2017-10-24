import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('CandidateNonAcademicWork e2e test', () => {

    let navBarPage: NavBarPage;
    let candidateNonAcademicWorkDialogPage: CandidateNonAcademicWorkDialogPage;
    let candidateNonAcademicWorkComponentsPage: CandidateNonAcademicWorkComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateNonAcademicWorks', () => {
        navBarPage.goToEntity('candidate-non-academic-work');
        candidateNonAcademicWorkComponentsPage = new CandidateNonAcademicWorkComponentsPage();
        expect(candidateNonAcademicWorkComponentsPage.getTitle()).toMatch(/gradzcircleApp.candidateNonAcademicWork.home.title/);

    });

    it('should load create CandidateNonAcademicWork dialog', () => {
        candidateNonAcademicWorkComponentsPage.clickOnCreateButton();
        candidateNonAcademicWorkDialogPage = new CandidateNonAcademicWorkDialogPage();
        expect(candidateNonAcademicWorkDialogPage.getModalTitle()).toMatch(/gradzcircleApp.candidateNonAcademicWork.home.createOrEditLabel/);
        candidateNonAcademicWorkDialogPage.close();
    });

    it('should create and save CandidateNonAcademicWorks', () => {
        candidateNonAcademicWorkComponentsPage.clickOnCreateButton();
        candidateNonAcademicWorkDialogPage.setNonAcademicInitiativeTitleInput('nonAcademicInitiativeTitle');
        expect(candidateNonAcademicWorkDialogPage.getNonAcademicInitiativeTitleInput()).toMatch('nonAcademicInitiativeTitle');
        candidateNonAcademicWorkDialogPage.setNonAcademicInitiativeDescriptionInput('nonAcademicInitiativeDescription');
        expect(candidateNonAcademicWorkDialogPage.getNonAcademicInitiativeDescriptionInput()).toMatch('nonAcademicInitiativeDescription');
        candidateNonAcademicWorkDialogPage.setDurationInput('5');
        expect(candidateNonAcademicWorkDialogPage.getDurationInput()).toMatch('5');
        candidateNonAcademicWorkDialogPage.getIsCurrentActivityInput().isSelected().then(function (selected) {
            if (selected) {
                candidateNonAcademicWorkDialogPage.getIsCurrentActivityInput().click();
                expect(candidateNonAcademicWorkDialogPage.getIsCurrentActivityInput().isSelected()).toBeFalsy();
            } else {
                candidateNonAcademicWorkDialogPage.getIsCurrentActivityInput().click();
                expect(candidateNonAcademicWorkDialogPage.getIsCurrentActivityInput().isSelected()).toBeTruthy();
            }
        });
        candidateNonAcademicWorkDialogPage.setRoleInInitiativeInput('roleInInitiative');
        expect(candidateNonAcademicWorkDialogPage.getRoleInInitiativeInput()).toMatch('roleInInitiative');
        candidateNonAcademicWorkDialogPage.setNonAcademicWorkStartDateInput('2000-12-31');
        expect(candidateNonAcademicWorkDialogPage.getNonAcademicWorkStartDateInput()).toMatch('2000-12-31');
        candidateNonAcademicWorkDialogPage.setNonAcademicWorkEndDateInput('2000-12-31');
        expect(candidateNonAcademicWorkDialogPage.getNonAcademicWorkEndDateInput()).toMatch('2000-12-31');
        candidateNonAcademicWorkDialogPage.candidateSelectLastOption();
        candidateNonAcademicWorkDialogPage.save();
        expect(candidateNonAcademicWorkDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateNonAcademicWorkComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-non-academic-work div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateNonAcademicWorkDialogPage {
    modalTitle = element(by.css('h4#myCandidateNonAcademicWorkLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nonAcademicInitiativeTitleInput = element(by.css('input#field_nonAcademicInitiativeTitle'));
    nonAcademicInitiativeDescriptionInput = element(by.css('input#field_nonAcademicInitiativeDescription'));
    durationInput = element(by.css('input#field_duration'));
    isCurrentActivityInput = element(by.css('input#field_isCurrentActivity'));
    roleInInitiativeInput = element(by.css('input#field_roleInInitiative'));
    nonAcademicWorkStartDateInput = element(by.css('input#field_nonAcademicWorkStartDate'));
    nonAcademicWorkEndDateInput = element(by.css('input#field_nonAcademicWorkEndDate'));
    candidateSelect = element(by.css('select#field_candidate'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNonAcademicInitiativeTitleInput = function (nonAcademicInitiativeTitle) {
        this.nonAcademicInitiativeTitleInput.sendKeys(nonAcademicInitiativeTitle);
    }

    getNonAcademicInitiativeTitleInput = function () {
        return this.nonAcademicInitiativeTitleInput.getAttribute('value');
    }

    setNonAcademicInitiativeDescriptionInput = function (nonAcademicInitiativeDescription) {
        this.nonAcademicInitiativeDescriptionInput.sendKeys(nonAcademicInitiativeDescription);
    }

    getNonAcademicInitiativeDescriptionInput = function () {
        return this.nonAcademicInitiativeDescriptionInput.getAttribute('value');
    }

    setDurationInput = function (duration) {
        this.durationInput.sendKeys(duration);
    }

    getDurationInput = function () {
        return this.durationInput.getAttribute('value');
    }

    getIsCurrentActivityInput = function () {
        return this.isCurrentActivityInput;
    }
    setRoleInInitiativeInput = function (roleInInitiative) {
        this.roleInInitiativeInput.sendKeys(roleInInitiative);
    }

    getRoleInInitiativeInput = function () {
        return this.roleInInitiativeInput.getAttribute('value');
    }

    setNonAcademicWorkStartDateInput = function (nonAcademicWorkStartDate) {
        this.nonAcademicWorkStartDateInput.sendKeys(nonAcademicWorkStartDate);
    }

    getNonAcademicWorkStartDateInput = function () {
        return this.nonAcademicWorkStartDateInput.getAttribute('value');
    }

    setNonAcademicWorkEndDateInput = function (nonAcademicWorkEndDate) {
        this.nonAcademicWorkEndDateInput.sendKeys(nonAcademicWorkEndDate);
    }

    getNonAcademicWorkEndDateInput = function () {
        return this.nonAcademicWorkEndDateInput.getAttribute('value');
    }

    candidateSelectLastOption = function () {
        this.candidateSelect.all(by.tagName('option')).last().click();
    }

    candidateSelectOption = function (option) {
        this.candidateSelect.sendKeys(option);
    }

    getCandidateSelect = function () {
        return this.candidateSelect;
    }

    getCandidateSelectedOption = function () {
        return this.candidateSelect.element(by.css('option:checked')).getText();
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
