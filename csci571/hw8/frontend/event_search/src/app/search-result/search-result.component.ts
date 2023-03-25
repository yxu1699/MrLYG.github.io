import { Component } from '@angular/core';
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
    console.log("getDetail", id)
    let iscontainEventData = false
    //get event--
    this.ticketmarketapiService.getEventById(id).subscribe(event => {
      console.log("event")
      console.log(event)

      if (event.error) {
        iscontainEventData = false
      } else {
        iscontainEventData = true
        console.log("ticketmarketapiService.getEventById(id). Not error")
        let eventname = null
        let date = null
        let time = null
        let artists = null
        let venue = null
        let genres = null
        let priceRanges = null
        let priceUnit = null
        let ticketStatus = null
        let imgurl = null
        let eventurl = null
        let twitterurl = null
        let facebookurl = null
        let venuename: any
        let eventid = null
        if (this.checkvalue(event.url)) {
          eventurl = event.url
        }
        if (this.checkvalue(event.name)) {
          eventname = event.name
        }
        if (this.checkvalue(event.dates.start.localDate)) {
          date = event.dates.start.localDate
        }
        if (this.checkvalue(event.dates.start.localTime)) {
          time = event.dates.start.localTime
        }
        if (this.checkvalue(event._embedded.attractions)) {
          let attractions = event._embedded.attractions
          artists = ''
          let x = 1
          for (let index = 0; index < attractions.length; index++) {
            if (!this.checkvalue(attractions[index].name)) {
              x = x + 1
              continue
            }
            artists = artists + attractions[index].name
            if (index != attractions.length - x) {
              artists = artists + " | "
            }
          }
        }
        if (this.checkvalue(event.seatmap)) {
          if (this.checkvalue(event.seatmap.staticUrl)) {
            imgurl = event.seatmap.staticUrl
          }

        }
        if (this.checkvalue(event._embedded.venues)) {
          venue = event._embedded.venues[0].name
          venuename = event._embedded.venues[0].name
        }
        // Genres – displays genre in the order of "segment", "genre", "subGenre", "type","subType",
        if (this.checkvalue(event.classifications)) {
          let cs = []
          if (this.checkvalue(event.classifications[0].segment) && this.checkvalue(event.classifications[0].segment.name)) {
            cs.push(event.classifications[0].segment)
          }

          if (this.checkvalue(event.classifications[0].genre) && this.checkvalue(event.classifications[0].genre.name)) {
            cs.push(event.classifications[0].genre)
          }

          if (this.checkvalue(event.classifications[0].subGenre) && this.checkvalue(event.classifications[0].subGenre.name)) {
            cs.push(event.classifications[0].subGenre)
          }

          if (this.checkvalue(event.classifications[0].type) && this.checkvalue(event.classifications[0].type.name)) {
            cs.push(event.classifications[0].type)
          }

          if (this.checkvalue(event.classifications[0].subType) && this.checkvalue(event.classifications[0].subType.name)) {
            cs.push(event.classifications[0].subType)
          }
          genres = ''
          if (cs.length > 0) {

            console.log(cs)

            for (let index = 0; index < cs.length; index++) {
              genres = genres + cs[index].name
              if (index != cs.length - 1) {
                genres = genres + " | "
              }
            }
          }
        }

        //priceRanges
        if (this.checkvalue(event.priceRanges)) {
          priceRanges = event.priceRanges[0].min + ' - ' + event.priceRanges[0].max
          if (this.checkvalue(event.priceRanges[0].currency)) {
            priceUnit = event.priceRanges[0].currency
          }
        }

        //ticketStatus
        if (this.checkvalue(event.dates.status)) {
          ticketStatus = event.dates.status.code
          // ticketStatus = ticketStatus.toLowerCase()
        }

        twitterurl =
          eventname +
          " on Ticketmaster.\r\n"
        twitterurl = encodeURIComponent(twitterurl)
        // twitterurl = twitterurl + "&url=" + eventurl
        // console.log("twitterurl",twitterurl)

        facebookurl =
          eventurl + "&amp;src=sdkpreparse"
        facebookurl = encodeURIComponent(facebookurl)
        let eventdetail = {
          'isContainData': iscontainEventData,
          'data': {
            "eventname": eventname,
            "localDate": date,
            "localTime": time,
            "artists": artists,
            "venue": venue,
            "genres": genres,
            "priceRanges": priceRanges,
            "priceUnit": priceUnit,
            "ticketStatus": ticketStatus,
            "imgurl": imgurl,
            "eventurl": eventurl,
            "facebookurl": facebookurl,
            "twitterurl": twitterurl,
            "eventid":event.id
          }
        }
        // console.log(eventdetail)
        console.log("eventdetail", eventdetail)
        let details = {
          "eventdetail": eventdetail,
          "artistsdetail": null,
          "venuedetail": null
        }
        this.searchResultMessageService.detailCard = details
        console.log("details", details)
        // artist -------------------------
        let isContainArtistData = false
        let attractions = event._embedded.attractions
        let artistinfo: any[] = []

        const getArtistInfo = (attraction: any) => {
          return new Promise<void>(async (resolve, reject) => {
            if (attraction.classifications[0].segment.name.toLowerCase() === 'music') {
              let artistName = attraction.name;
              this.ticketmarketapiService.getSpotifyArtistInfo(artistName).subscribe(async data => {
                if (this.checkvalue(data.artists.items)) {
                  let items = data.artists.items;
                  const asyncTasks = items.map(async (item: any) => {
                    if (artistName.toLowerCase() === item.name.toLowerCase()) {
                      console.log("artist from spotify", item);
                      let itemdata = await this.itemabstract(item);
                      itemdata.artistname = artistName;
                      artistinfo.push(itemdata);
                    }
                  });
                  try {
                    await Promise.all(asyncTasks);
                    resolve();
                  } catch (error) {
                    reject('Error while processing artist info');
                  }
                } else {
                  reject('No artist info found');
                }
              });
            } else {
              resolve();
            }
          });
        };

        // const getArtistInfo = (attraction: any) => {
        //   return new Promise<void>((resolve, reject) => {
        //     if (attraction.classifications[0].segment.name.toLowerCase() === 'music') {
        //       let artistName = attraction.name;
        //       this.ticketmarketapiService.getSpotifyArtistInfo(artistName).subscribe(data => {
        //         if (this.checkvalue(data.artists.items)) {
        //           let items = data.artists.items;
        //           // items.forEach(async (item: any) => {
        //           //   if (artistName.toLowerCase() === item.name.toLowerCase()) {
        //           //     console.log("artist from spotify", item)
        //           //     let itemdata = await this.itemabstract(item);
        //           //     itemdata.artistname = artistName;
        //           //     artistinfo.push(itemdata);
        //           //   }
        //           // });
        //           const asyncTasks = items.map(async (item: any) => {
        //             if (artistName.toLowerCase() === item.name.toLowerCase()) {
        //               console.log("artist from spotify", item);
        //               let itemdata = await this.itemabstract(item);
        //               itemdata.artistname = artistName;
        //               artistinfo.push(itemdata);
        //             }
        //           });
        //           await Promise.all(asyncTasks);
        //           resolve();
        //         } else {
        //           reject('No artist info found');
        //         }
        //       });
        //     } else {
        //       resolve();
        //     }
        //   });
        // };
        let artistsdetail = {}
        const processAttractions = async () => {
          console.log("get attractions", attractions)
          if (this.checkvalue(attractions)) {
            // for (let i = 0; i < attractions.length; i++) {
            //   await getArtistInfo(attractions[i]);
            // }
            const promises = []
            for (let i = 0; i < attractions.length; i++) {
              // await getArtistInfo(attractions[i]);
              promises.push(getArtistInfo(attractions[i]))
            }
            for await (let result of promises) {
              console.log("await all promises result", result)
            }
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

          //venue--------------------
          let isContainVenueData = false
          // console.log
          this.ticketmarketapiService.getVenueByName(venuename).subscribe(venue => {
            console.log("venue detail")
            console.log(venue)
            let venuedata = null
            if (venue.error || venue.page.totalElements === 0) {
              isContainVenueData = false
            } else {
              isContainVenueData = true
              venuedata = this.venueabstract(venue._embedded.venues[0])
            }
            let venuedetail = {
              'isContainData': isContainVenueData,
              'data': venuedata
            }
            console.log("venuedetail", venuedetail)

            let details = {
              "eventdetail": eventdetail,
              "artistsdetail": artistsdetail,
              "venuedetail": venuedetail
            }
            this.searchResultMessageService.detailCard = details
            console.log("details", details)
          })
        };




        processAttractions();

        // attractions.forEach((attraction: any) => {
        //   if (attraction.classifications[0].segment.name.toLowerCase() === 'music') {
        //     let artistName = attraction.name
        //     this.ticketmarketapiService.getSpotifyAryisyInfo(artistName).subscribe(data => {
        //       if (this.checkvalue(data.artists.items)) {
        //         let items = data.artists.items
        //         items.forEach((item: any) => {
        //           if (artistName.toLowerCase() === item.name.toLowerCase()) {
        //             let itemdata = this.itemabstract(item)
        //             itemdata.artistname = artistName
        //             artistinfo.push(itemdata)
        //           }
        //         });
        //       }
        //     })
        //   }
        // });


      }
    })

    //get artist--

    //get venue--

    //data to a golbal service

    //删除result table  
    this.searchResultMessageService.serachResultisShow = false
    // show detail card
    this.searchResultMessageService.detailCardisShow = true
  }

  venueabstract(venue: any) {
    let address = null
    let city = null
    let phonenumber = null
    let openhours = null
    let generalrule = null
    let childrule = null
    let name = null
    let latitude :number | null = null
    let longitude :number | null = null
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
    if (this.checkvalue(venue.location.latitude)){
      latitude = parseFloat(venue.location.latitude)
      
    }
    if (this.checkvalue(venue.location.longitude)){
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
      "latitude":latitude,
      "longitude":longitude
    }
  }

  checkvalue(x: any) {
    if (x !== null && typeof x !== "undefined" && x !== "Undefined") {
      return true
    }
    return false
  }

  async itemabstract(item: any): Promise<any> {
    let name = null
    let followers = null
    let popularity = null
    let spotifyLink = null
    let images = null
    let artistid = null
    let album = null
    if (this.checkvalue(item.name)) {
      name = item.name
    }
    if (this.checkvalue(item.followers) && this.checkvalue(item.followers.total)) {
      followers = item.followers.total
      let num = parseInt(followers);
      let formattedNumber = new Intl.NumberFormat().format(num);
      followers = formattedNumber
    }
    if (this.checkvalue(item.popularity)) {
      popularity = item.popularity
    }
    if (this.checkvalue(item.external_urls)) {
      spotifyLink = item.external_urls.spotify

    }
    if (this.checkvalue(item.images)) {
      images = item.images
    }
    if (this.checkvalue(item.id)) {
      artistid = item.id
    }

    try {
      let data = await lastValueFrom(this.ticketmarketapiService.getSpotifyArtistAlbum(artistid));
      album = data;
    } catch (error) {
      console.error('Error while fetching artist album:', error);
    }
    // album = this.ticketmarketapiService.getSpotifyArtistAlbum(artistid)
    // .subscribe(data => {
    //   album = data;
    // })
    return {
      'name': name,
      'followers': followers,
      'popularity': popularity,
      'spotifyLink': spotifyLink,
      'images': images,
      'album': album
    }

  }
}



