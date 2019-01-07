import {Injectable} from '@angular/core';

@Injectable()
export class DataService {
  private routeData: any;

  private dataMap: Map<string, string>;

  constructor() {
    this.dataMap = new Map<string , string>();
  }

  public setRouteData(data) {
    this.routeData = data;
  }

  public getRouteData() {
    return this.routeData;
  }

  put(key, value) {
    this.dataMap.set(key, value);
  }

  get(key): string {
    return this.dataMap.get(key);
  }

}