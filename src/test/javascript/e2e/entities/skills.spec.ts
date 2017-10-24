import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Skills e2e test', () => {

    let navBarPage: NavBarPage;
    let skillsDialogPage: SkillsDialogPage;
    let skillsComponentsPage: SkillsComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Skills', () => {
        navBarPage.goToEntity('skills');
        skillsComponentsPage = new SkillsComponentsPage();
        expect(skillsComponentsPage.getTitle()).toMatch(/gradzcircleApp.skills.home.title/);

    });

    it('should load create Skills dialog', () => {
        skillsComponentsPage.clickOnCreateButton();
        skillsDialogPage = new SkillsDialogPage();
        expect(skillsDialogPage.getModalTitle()).toMatch(/gradzcircleApp.skills.home.createOrEditLabel/);
        skillsDialogPage.close();
    });

    it('should create and save Skills', () => {
        skillsComponentsPage.clickOnCreateButton();
        skillsDialogPage.setSkillInput('skill');
        expect(skillsDialogPage.getSkillInput()).toMatch('skill');
        skillsDialogPage.save();
        expect(skillsDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SkillsComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-skills div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SkillsDialogPage {
    modalTitle = element(by.css('h4#mySkillsLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    skillInput = element(by.css('input#field_skill'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setSkillInput = function (skill) {
        this.skillInput.sendKeys(skill);
    }

    getSkillInput = function () {
        return this.skillInput.getAttribute('value');
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
