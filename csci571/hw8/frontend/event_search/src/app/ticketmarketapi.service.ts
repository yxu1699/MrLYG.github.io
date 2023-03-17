import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class TicketmarketapiService {
  constructor(private http: HttpClient) { }

  getTickects(keyword: string, distance: number, category: string, lat: string, lng: string): Observable<any> {
    let url = "https://nodejs-379321.uw.r.appspot.com/tickets?" +
      'keyword=' + keyword + '&distance=' + distance + '&category=' + category + "&latitude=" + lat +
      '&longitude=' + lng
    return this.http.get<any>(url);
  }

}
