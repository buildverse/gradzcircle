import {Injectable} from '@angular/core';
import {LocalStorageService} from 'ngx-webstorage';


@Injectable()
export class DataStorageService {


  constructor(private storageService: LocalStorageService) {
    //this.securityKey = new SecureLS({encodingType: 'rc4', isCompression: false, encryptionSecret: '#D4r18i9s19h8i9k11a1#$'});
  }

  setdata(key, value) {
    this.storageService.store(key, value);
  }

  getData(key): any {
    return this.storageService.retrieve(key);
  }
}