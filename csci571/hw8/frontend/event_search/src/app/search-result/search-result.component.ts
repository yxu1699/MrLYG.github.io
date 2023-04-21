import { Component, ViewChild, ElementRef } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';
import { TicketmarketapiService } from '../ticketmarketapi.service';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent {

  constructor(public searchResultMessageService: SearchResultMessageService, private ticketmarketapiService: TicketmarketapiService) {

  }

  detail: any
  /*{
    eventdetail:{
      'iscontainData': true,
      'data': {
        Date,
        Artists/Team – displays artists/team “name” segmented by “ | ”,
        Venue,
        Genres – displays genre in the order of "segment", "genre", "subGenre", "type",
        "subType",
        Price Ranges,
        Ticket Status,
      },
      {

      }
    },
    artist:{
      'iscontainData': true,
      'data': {}
    },
    venue:{
      'iscontainData': true,
      'data': {}
    },
  }

   */
  //recieve id get event detail
  getDetail(id: any) {
    this.ticketmarketapiService.getEventById(id).subscribe(event => {
      if (event.error) {
      } else {
        let isContainArtistData = false;
        let attractions = event._embedded.attractions;
        let artistinfo: any[] = [];
        const processItems = async (items: any[], artistName: string) => {
          for (const item of items) {
            if (artistName.toLowerCase() === item.name.toLowerCase()) {
              console.log("artist from spotify", item);
              let itemdata = await this.itemabstract(item);
              itemdata.artistname = artistName;
              return itemdata;
            }
          }
        };
        const getArtistInfo = (attraction: any) => {
          return new Promise<any>((resolve, reject) => {
            if (attraction.classifications[0].segment.name.toLowerCase() === "music") {
              let artistName = attraction.name;
              this.ticketmarketapiService.getSpotifyArtistInfo(artistName).subscribe(data => {
                if (this.checkvalue(data.artists.items)) {
                  let items = data.artists.items;
                  processItems(items, artistName)
                    .then(result => {
                      resolve(result);
                    })
                    .catch(error => {
                      reject(error);
                    });
                } else {
                  resolve(null);
                }
              });
            } else {
              resolve(null);
            }
          });
        };

        let artistsdetail = {}
        const processAttractions = async () => {
          console.log("get attractions", attractions)
          if (this.checkvalue(attractions)) {
            const promises = attractions.map((attraction: any) => getArtistInfo(attraction));
            artistinfo = (await Promise.all(promises)).filter(result => result !== null);
          }
          console.log(artistinfo.length)
          if (artistinfo.length > 0) {
            isContainArtistData = true
          }
          artistsdetail = {
            'isContainData': isContainArtistData,
            'data': artistinfo
          }
          console.log("artistsdetail", artistsdetail)
        };
        processAttractions();
      }
    })
  }
  checkvalue(x: any) {
    if (x !== null && typeof x !== "undefined" && x !== "Undefined") {
      return true
    }
    return false
  }


  venueabstract(venue: any) {
    let address = null
    let city = null
    let phonenumber = null
    let openhours = null
    let generalrule = null
    let childrule = null
    let name = null
    let latitude: number | null = null
    let longitude: number | null = null
    if (this.checkvalue(venue.name)) {
      name = venue.name
    }

    if (this.checkvalue(venue.address) && this.checkvalue(venue.address.line1)) {
      address = venue.address.line1
    }
    let cityname = ""
    let statename = ""
    if (this.checkvalue(venue.state) && this.checkvalue(venue.state.name)) {
      statename = venue.state.name
    }
    if (this.checkvalue(venue.city) && this.checkvalue(venue.city.name)) {
      cityname = venue.city.name
    }
    if ((this.checkvalue(venue.state) && this.checkvalue(venue.state.name)) || (this.checkvalue(venue.city) && this.checkvalue(venue.city.name))) {
      city = cityname + ", " + statename
    }

    //phonenumber openhours
    if (this.checkvalue(venue.boxOfficeInfo)) {
      if (this.checkvalue(venue.boxOfficeInfo.phoneNumberDetail)) {
        phonenumber = venue.boxOfficeInfo.phoneNumberDetail
      }
      if (this.checkvalue(venue.boxOfficeInfo.openHoursDetail)) {
        openhours = venue.boxOfficeInfo.openHoursDetail
      }
    }

    //generalrule childrule
    if (this.checkvalue(venue.generalInfo)) {
      if (this.checkvalue(venue.generalInfo.childRule)) {
        childrule = venue.generalInfo.childRule
      }
      if (this.checkvalue(venue.generalInfo.generalRule)) {
        generalrule = venue.generalInfo.generalRule
      }
    }
    if (this.checkvalue(venue.location.latitude)) {
      latitude = parseFloat(venue.location.latitude)

    }
    if (this.checkvalue(venue.location.longitude)) {
      longitude = parseFloat(venue.location.longitude)
    }

    return {
      'name': name,
      'address': address,
      'city': city,
      'phonenumber': phonenumber,
      'openhours': openhours,
      'generalrule': generalrule,
      'childrule': childrule,
      "latitude": latitude,
      "longitude": longitude
    }
  }

  
}





