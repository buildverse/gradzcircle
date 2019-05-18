import {Injectable} from '@angular/core';
import {LocalStorageService} from 'ngx-webstorage';

@Injectable()
export class DataService {

  constructor(private storageService: LocalStorageService) {
  }

  public setRouteData(data) {
    this.storageService.store('', data);
  }

  public getRouteData() {
    return this.storageService.retrieve('');
  }

  put(key, value) {
    this.storageService.store(key, value);
  }

  get(key): string {
    return this.storageService.retrieve(key);
  }

}