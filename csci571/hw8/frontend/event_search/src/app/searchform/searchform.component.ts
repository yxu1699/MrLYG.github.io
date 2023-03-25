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


  convertDataToEvents(data: any) {
    let res = {
      'iscontainData': false,
      'data': []
    }

    if (data.page.totalElements <= 0) {
      return res
    }


    let events = data._embedded.events.sort((a: any, b: any) => {
      let ad = new Date(a.dates.start.localDate)
      let at = ad.getTime()
      let bd = new Date(b.dates.start.localDate)
      let bt = bd.getTime()
      return at - bt
    })

    // type ev = {
    //   date: string;
    //   time: string;
    //   icon: string;
    //   eventname: string;
    //   genre: string;
    //   venue: string;
    // }
    let evens :any[] = []
    // for (let i = 0; i < events.length; i++) {
    //   let event = events[i];
    //   let date = null
    //   let time = null
    //   let icon = null
    //   let eventname = null
    //   let genre = null
    //   let venue = null
    //   if (this.checkvalue(event.dates.start.localDate)) {
    //     date = event.dates.start.localDate
    //   }
    //   if (this.checkvalue(event.dates.start.localTime)) {
    //     time = event.dates.start.localTime
    //   }
    //   if (this.checkvalue(event.images)) {
    //     icon = event.images
    //   }
    //   if (this.checkvalue(event.name)) {
    //     eventname = event.name
    //   }
    //   if (this.checkvalue(event.classifications) && this.checkvalue(event.classifications[0].segment.name)) {
    //     genre = event.classifications[0].segment.name
    //   }
    //   if (this.checkvalue(event._embedded.venues) && this.checkvalue(event._embedded.venues[0].name)) {
    //     venue = event._embedded.venues[0].name
    //   }

    //   let ele = {
    //     'date': date,
    //     'time': time,
    //     'icon': icon,
    //     'eventname': eventname,
    //     'genre': genre,
    //     'venue': venue
    //   }
    //   evens[i] 
      

    // }
    events.forEach((event: any) => {
      let date = null
      let time = null
      let icon = null
      let eventname = null
      let genre = null
      let venue = null
      if (this.checkvalue(event.dates.start.localDate)) {
        date = event.dates.start.localDate
      }
      if (this.checkvalue(event.dates.start.localTime)) {
        time = event.dates.start.localTime
      }
      if (this.checkvalue(event.images)) {
        icon = event.images[0].url
      }
      if (this.checkvalue(event.name)) {
        eventname = event.name
      }
      if (this.checkvalue(event.classifications) && this.checkvalue(event.classifications[0].segment.name)) {
        genre = event.classifications[0].segment.name
      }
      if (this.checkvalue(event._embedded.venues) && this.checkvalue(event._embedded.venues[0].name)) {
        venue = event._embedded.venues[0].name
      }

      let ele = {
        'id':event.id,
        'date': date,
        'time': time,
        'icon': icon,
        'eventname': eventname,
        'genre': genre,
        'venue': venue
      }
      evens.push(ele)
      

    });
    console.log("serachFormEvents",evens)
    return {
      'iscontainData': true,
      'data': evens
    }
    
  }

  submitForSearch(lat: string, lng: string) {
    // get lat and long
    console.log(this.keyword, this.distance, this.category, lat, lng)
    this.ticketmarketapiService.getTickects(this.keyword, this.distance, this.category, lat, lng).subscribe(data => {
      console.log(data)
      //把数据封装到event{}-->把数据传输给service
      this.searchResultMessageService.serachResult = this.convertDataToEvents(data)
      this.searchResultMessageService.serachResultisShow = true
      console.log( this.searchResultMessageService.serachResult)
    })



  }

  submitForm() {
    if (!this.isAutoFindLocation) {
      this.geocodingService.getLatLng(this.location).subscribe(data => {
        console.log("input get geo")
        if (data.error !== null && typeof data.error !== "undefined") {
          console.log(data)
          console.log(data.error)
          console.log("input get geo has error")
          this.geocodingService.getLatLngAuto().subscribe(data => {
            let loc = data.loc
            this.submitForSearch(loc.substring(0, loc.indexOf(',')), loc.substring(loc.indexOf(',') + 1))
          })
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
    this.isAutoFindLocation = false
    this.searchResultMessageService.detailCard = null;
    this.searchResultMessageService.detailCardisShow = false;
  }



  checkvalue(x: any) {
    if (x !== null && typeof x !== "undefined" && x !== "Undefined") {
      return true
    }
    return false
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
