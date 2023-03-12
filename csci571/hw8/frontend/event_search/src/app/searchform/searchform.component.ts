import axios from 'axios';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
//app.component.ts
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GeocodingService } from '../geocoding.service';
import { TicketmarketapiService } from '../ticketmarketapi.service'
import { SearchResultMessageService } from '../search-result-message.service';
@Component({
  selector: 'app-searchform',
  templateUrl: './searchform.component.html',
  styleUrls: ['./searchform.component.css']
})
export class SearchformComponent implements OnInit {

  keyword: string = "";
  distance: number = 10;
  category: string = "";
  location: string = "";
  public availableCategory: any = [
    "Default",
    "Music",
    "Sports",
    "Arts & Theatre",
    "Film",
    "Miscellaneous"
  ]

  isAutoFindLocation: boolean = false;

  constructor(private route: ActivatedRoute, private http: HttpClient,
    private geocodingService: GeocodingService, private ticketmarketapiService: TicketmarketapiService,
    private searchResultMessageService: SearchResultMessageService) {
    this.category = this.availableCategory[0];
  }






  // control loction input text
  locationCheckBoxChange() {
    console.log(this.isAutoFindLocation)
    this.isAutoFindLocation = !this.isAutoFindLocation
    this.location = ""
  }





  submitForSearch(lat: string, lng: string) {
    // get lat and long
    console.log(lat)
    console.log(lng)

    console.log(this.keyword, this.distance, this.category, lat, lng)
    this.ticketmarketapiService.getTickects(this.keyword, this.distance, this.category, lat, lng).subscribe(data => {
      console.log(data)
      this.searchResultMessageService.serachResult = data
    })



  }

  submitForm() {
    if (!this.isAutoFindLocation) {
      this.geocodingService.getLatLng(this.location).subscribe(data => {

        if (data.error !== null) {
          this.geocodingService.getLatLngAuto().subscribe(data => {
            let loc = data.loc
            this.submitForSearch(loc.substring(0, loc.indexOf(',')), loc.substring(loc.indexOf(',') + 1))
          }
          )
        } else {
          this.submitForSearch(data.lat, data.lng)
        }

      })
    } else {
      this.geocodingService.getLatLngAuto().subscribe(data => {
        let loc = data.loc
        this.submitForSearch(loc.substring(0, loc.indexOf(',')), loc.substring(loc.indexOf(',') + 1))
      }
      )
    }
  }






  clear(): void {
    this.keyword = "";
    this.distance = 10;
    this.category = this.availableCategory[0];
    this.location = "";
    this.searchResultMessageService.serachResult = null;
  }








  /** auto complete */
  suggestCtrl = new FormControl();
  filteredSuggestes: any;
  isLoading = false;
  errorMsg!: string;
  minLengthTerm = 1;



  onSelected() {
    console.log("selected")
    console.log(this.keyword);
    this.keyword = this.keyword;
  }

  displayWith(value: any) {
    return value;
  }

  clearSelection() {
    console.log("clear")
    this.keyword = "";
    this.filteredSuggestes = [];
  }

  ngOnInit() {
    this.suggestCtrl.valueChanges
      .pipe(
        filter(res => {
          return res !== null && res.length >= this.minLengthTerm
        }),
        distinctUntilChanged(),
        debounceTime(300),
        tap(() => {
          this.errorMsg = "";
          this.filteredSuggestes = [];
          this.isLoading = true;
        }),
        switchMap(value => this.http.get('https://nodejs-379321.uw.r.appspot.com/suggest?keyword=' + value)
          .pipe(
            finalize(() => {
              this.isLoading = false
            }),
          )
        )
      )
      .subscribe((data: any) => {
        if (data['suggest'] == undefined) {
          this.errorMsg = data['error'];
          this.filteredSuggestes = [];
        } else {
          this.errorMsg = "";
          this.filteredSuggestes = data['suggest'];
        }
        console.log("get all suggestes")
        console.log(this.filteredSuggestes);
      });
  }

}
