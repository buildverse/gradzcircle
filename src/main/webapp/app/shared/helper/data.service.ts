import {Injectable} from '@angular/core';

@Injectable()
export class DataService {
  public columnVars: any;
  private routeData: any;
  constructor() {
  }
  
  public setRouteData(data) {
    this.routeData = data;
  }
  
  public getRouteData() {
    return this.routeData;
  }
}