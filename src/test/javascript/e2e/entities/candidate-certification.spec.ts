import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CandidateCertification e2e test', () => {

    let navBarPage: NavBarPage;
    let candidateCertificationDialogPage: CandidateCertificationDialogPage;
    let candidateCertificationComponentsPage: CandidateCertificationComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateCertifications', () => {
        navBarPage.goToEntity('candidate-certification');
        candidateCertificationComponentsPage = new CandidateCertificationComponentsPage();
        expect(candidateCertificationComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.candidateCertification.home.title/);

    });

    it('should load create CandidateCertification dialog', () => {
        candidateCertificationComponentsPage.clickOnCreateButton();
        candidateCertificationDialogPage = new CandidateCertificationDialogPage();
        expect(candidateCertificationDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.candidateCertification.home.createOrEditLabel/);
        candidateCertificationDialogPage.close();
    });

    it('should create and save CandidateCertifications', () => {
        candidateCertificationComponentsPage.clickOnCreateButton();
        candidateCertificationDialogPage.setCertificationTitleInput('certificationTitle');
        expect(candidateCertificationDialogPage.getCertificationTitleInput()).toMatch('certificationTitle');
        candidateCertificationDialogPage.setCertificationDateInput('2000-12-31');
        expect(candidateCertificationDialogPage.getCertificationDateInput()).toMatch('2000-12-31');
        candidateCertificationDialogPage.setCertificationDetailsInput('certificationDetails');
        expect(candidateCertificationDialogPage.getCertificationDetailsInput()).toMatch('certificationDetails');
        candidateCertificationDialogPage.candidateSelectLastOption();
        candidateCertificationDialogPage.save();
        expect(candidateCertificationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateCertificationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-certification div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateCertificationDialogPage {
    modalTitle = element(by.css('h4#myCandidateCertificationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    certificationTitleInput = element(by.css('input#field_certificationTitle'));
    certificationDateInput = element(by.css('input#field_certificationDate'));
    certificationDetailsInput = element(by.css('input#field_certificationDetails'));
    candidateSelect = element(by.css('select#field_candidate'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCertificationTitleInput = function(certificationTitle) {
        this.certificationTitleInput.sendKeys(certificationTitle);
    };

    getCertificationTitleInput = function() {
        return this.certificationTitleInput.getAttribute('value');
    };

    setCertificationDateInput = function(certificationDate) {
        this.certificationDateInput.sendKeys(certificationDate);
    };

    getCertificationDateInput = function() {
        return this.certificationDateInput.getAttribute('value');
    };

    setCertificationDetailsInput = function(certificationDetails) {
        this.certificationDetailsInput.sendKeys(certificationDetails);
    };

    getCertificationDetailsInput = function() {
        return this.certificationDetailsInput.getAttribute('value');
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
