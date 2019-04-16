
import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRouteSnapshot, NavigationEnd, NavigationStart, RouterEvent, NavigationCancel, NavigationError} from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import {JhiLanguageHelper} from '../../shared';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: [
    'main.css'
  ]
})
export class JhiMainComponent implements OnInit {

  constructor(
    private jhiLanguageHelper: JhiLanguageHelper,
    private router: Router,
    private spinnerService: NgxSpinnerService
  ) {
    this.router.events.subscribe((event: RouterEvent) => {
      if (event instanceof NavigationStart) {
        this.spinnerService.show();
      }
      if (event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError) {
        this.spinnerService.hide();

      }
    });

  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'gradzcircleApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  ngOnInit() {
   this.spinnerService.show();
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));

      }
    });
  }
}
