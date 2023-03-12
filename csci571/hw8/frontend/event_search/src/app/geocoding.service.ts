import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GeocodingService {

  constructor(private http: HttpClient) { }

  getLatLng(loc:string) : Observable<any> {
    let url = 'https://nodejs-379321.uw.r.appspot.com/latlong?location=' + loc;
    return this.http.get<any>(url);
  }
  getLatLngAuto() : Observable<any> {
    let url = 'https://ipinfo.io/?token=' + "0c71fd7bc99cde";
    return this.http.get<any>(url);
  }
}
