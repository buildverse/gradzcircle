import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, CanDeactivate } from '@angular/router';
import { CandidateProfileAboutMeEditComponent } from './candidate-about-me-edit.component';


@Injectable()


export  class CandidateAboutMeEditGuard implements CanDeactivate<CandidateProfileAboutMeEditComponent> {

    canDeactivate(component: CandidateProfileAboutMeEditComponent): boolean {
        if (component.candidateAboutMeForm.dirty) {
            //let candidateName = component.candidateAboutMeForm.get('productName').value || 'New Product';
            return confirm(`Navigate away and lose all changes to your profile ?`);
        }
        return true;
    }
}