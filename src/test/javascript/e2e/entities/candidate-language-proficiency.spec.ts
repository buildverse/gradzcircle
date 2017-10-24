import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('CandidateLanguageProficiency e2e test', () => {

    let navBarPage: NavBarPage;
    let candidateLanguageProficiencyDialogPage: CandidateLanguageProficiencyDialogPage;
    let candidateLanguageProficiencyComponentsPage: CandidateLanguageProficiencyComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateLanguageProficiencies', () => {
        navBarPage.goToEntity('candidate-language-proficiency');
        candidateLanguageProficiencyComponentsPage = new CandidateLanguageProficiencyComponentsPage();
        expect(candidateLanguageProficiencyComponentsPage.getTitle()).toMatch(/gradzcircleApp.candidateLanguageProficiency.home.title/);

    });

    it('should load create CandidateLanguageProficiency dialog', () => {
        candidateLanguageProficiencyComponentsPage.clickOnCreateButton();
        candidateLanguageProficiencyDialogPage = new CandidateLanguageProficiencyDialogPage();
        expect(candidateLanguageProficiencyDialogPage.getModalTitle()).toMatch(/gradzcircleApp.candidateLanguageProficiency.home.createOrEditLabel/);
        candidateLanguageProficiencyDialogPage.close();
    });

    it('should create and save CandidateLanguageProficiencies', () => {
        candidateLanguageProficiencyComponentsPage.clickOnCreateButton();
        candidateLanguageProficiencyDialogPage.setProficiencyInput('proficiency');
        expect(candidateLanguageProficiencyDialogPage.getProficiencyInput()).toMatch('proficiency');
        candidateLanguageProficiencyDialogPage.candidateSelectLastOption();
        candidateLanguageProficiencyDialogPage.languageSelectLastOption();
        candidateLanguageProficiencyDialogPage.save();
        expect(candidateLanguageProficiencyDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateLanguageProficiencyComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-language-proficiency div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateLanguageProficiencyDialogPage {
    modalTitle = element(by.css('h4#myCandidateLanguageProficiencyLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    proficiencyInput = element(by.css('input#field_proficiency'));
    candidateSelect = element(by.css('select#field_candidate'));
    languageSelect = element(by.css('select#field_language'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setProficiencyInput = function (proficiency) {
        this.proficiencyInput.sendKeys(proficiency);
    }

    getProficiencyInput = function () {
        return this.proficiencyInput.getAttribute('value');
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

    languageSelectLastOption = function () {
        this.languageSelect.all(by.tagName('option')).last().click();
    }

    languageSelectOption = function (option) {
        this.languageSelect.sendKeys(option);
    }

    getLanguageSelect = function () {
        return this.languageSelect;
    }

    getLanguageSelectedOption = function () {
        return this.languageSelect.element(by.css('option:checked')).getText();
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
