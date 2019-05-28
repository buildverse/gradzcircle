import {PASSWORDVALUE,PASSWORDKEY} from '../constants/storage.constants';
import {Injectable} from '@angular/core';
import {LocalStorageService} from 'ngx-webstorage';
import * as CryptoJS from 'crypto-js';


@Injectable()
export class DataStorageService {


  constructor(private storageService: LocalStorageService) {
    //this.securityKey = new SecureLS({encodingType: 'rc4', isCompression: false, encryptionSecret: '#D4r18i9s19h8i9k11a1#$'});
  }

  setdata(key, value) {
   // console.log('setting data for key  '+key+'   its vlaue is '+ JSON.stringify(value.toString().trim()));
    this.storageService.store(CryptoJS.SHA3(key).toString(), CryptoJS.AES.encrypt(value.toString().trim(), PASSWORDVALUE.trim()).toString());
  }

  getData(key): any {
  //  console.log('getting data for key  '+key+'   its encrypted vlaue is '+CryptoJS.SHA3(key));
    let value = '';
    if (key) {
      key = CryptoJS.SHA3(key).toString();
      if (this.storageService.retrieve(key)) {
        value = CryptoJS.AES.decrypt(this.storageService.retrieve(key), PASSWORDVALUE.trim()).toString(CryptoJS.enc.Utf8);
     //   console.log('Returning for incoming key '+incomeingKey+' value '+JSON.stringify(value));
        return value;
      }
    }
  }

  removeData(key) {
    key = CryptoJS.SHA3(key).toString();
    this.storageService.clear(key);
  }

}