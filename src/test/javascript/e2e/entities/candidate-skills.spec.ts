import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CandidateSkills e2e test', () => {

    let navBarPage: NavBarPage;
    let candidateSkillsDialogPage: CandidateSkillsDialogPage;
    let candidateSkillsComponentsPage: CandidateSkillsComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateSkills', () => {
        navBarPage.goToEntity('candidate-skills');
        candidateSkillsComponentsPage = new CandidateSkillsComponentsPage();
        expect(candidateSkillsComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.candidateSkills.home.title/);

    });

    it('should load create CandidateSkills dialog', () => {
        candidateSkillsComponentsPage.clickOnCreateButton();
        candidateSkillsDialogPage = new CandidateSkillsDialogPage();
        expect(candidateSkillsDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.candidateSkills.home.createOrEditLabel/);
        candidateSkillsDialogPage.close();
    });

    it('should create and save CandidateSkills', () => {
        candidateSkillsComponentsPage.clickOnCreateButton();
        candidateSkillsDialogPage.setSkillInput('skill');
        expect(candidateSkillsDialogPage.getSkillInput()).toMatch('skill');
        candidateSkillsDialogPage.candidateSelectLastOption();
        candidateSkillsDialogPage.skillsSelectLastOption();
        candidateSkillsDialogPage.save();
        expect(candidateSkillsDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateSkillsComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-skills div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateSkillsDialogPage {
    modalTitle = element(by.css('h4#myCandidateSkillsLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    skillInput = element(by.css('input#field_skill'));
    candidateSelect = element(by.css('select#field_candidate'));
    skillsSelect = element(by.css('select#field_skills'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setSkillInput = function(skill) {
        this.skillInput.sendKeys(skill);
    };

    getSkillInput = function() {
        return this.skillInput.getAttribute('value');
    };

    candidateSelectLastOption = function() {
        this.candidateSelect.all(by.tagName('option')).last().click();
    };

    candidateSelectOption = function(option) {
        this.candidateSelect.sendKeys(option);
    };

    getCandidateSelect = function() {
        return this.candidateSelect;
    };

    getCandidateSelectedOption = function() {
        return this.candidateSelect.element(by.css('option:checked')).getText();
    };

    skillsSelectLastOption = function() {
        this.skillsSelect.all(by.tagName('option')).last().click();
    };

    skillsSelectOption = function(option) {
        this.skillsSelect.sendKeys(option);
    };

    getSkillsSelect = function() {
        return this.skillsSelect;
    };

    getSkillsSelectedOption = function() {
        return this.skillsSelect.element(by.css('option:checked')).getText();
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
