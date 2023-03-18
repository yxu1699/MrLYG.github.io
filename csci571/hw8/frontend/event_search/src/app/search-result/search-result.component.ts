import { Component } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';
import { TicketmarketapiService } from '../ticketmarketapi.service';

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
        let goAHeadNextSearch = false
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
          for (let index = 0; index < attractions.length; index++) {
            artists = artists + attractions[index].name
            if (index != attractions.length - 1) {
              artists = artists + " | "
            }
          }
        }
        if (this.checkvalue(event._embedded.venues)) {
          venue = event._embedded.venues[0].name
        }
        // Genres – displays genre in the order of "segment", "genre", "subGenre", "type","subType",
        if (this.checkvalue(event.classifications)) {
          let cs = []
          if (this.checkvalue(event.classifications[0].segment)) {
            cs.push(event.classifications[0].segment)
          }

          if (this.checkvalue(event.classifications[0].genre)) {
            cs.push(event.classifications[0].genre)
          }

          if (this.checkvalue(event.classifications[0].subGenre)) {
            cs.push(event.classifications[0].subGenre)
          }

          if (this.checkvalue(event.classifications[0].type)) {
            cs.push(event.classifications[0].type)
          }

          if (this.checkvalue(event.classifications[0].subType)) {
            cs.push(event.classifications[0].subType)
          }
          genres = ''
          if (cs.length > 0) {
            console.log(cs)
            for (let index = 0; index < cs.length; index++) {
              genres = genres + cs[index].name
              if ( genres.toLowerCase() === 'music'){
                goAHeadNextSearch = true
              }
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
          ticketStatus = event.dates.status.code.toLowerCase()
          // ticketStatus = ticketStatus.toLowerCase()
        }

        let eventdetail = {
          'isContainData': iscontainEventData,
          'data': {
            "localDate": date,
            "localTime": time,
            "artists": artists,
            "venue": venue,
            "genres": genres,
            "priceRanges": priceRanges,
            "priceUnit": priceUnit,
            "ticketStatus": ticketStatus,
          }
        }
        console.log(eventdetail)
        if(goAHeadNextSearch){
          
        }else{

        }

      }


      let artistName = event._embedded.attractions[0].name
      // if genre contains music **need sure?
      // this.ticketmarketapiService.getSpotifyAryisyInfo(artistName).subscribe(data => {
      // //get venue -- need backend

      // })



    })

    //get artist--

    //get venue--

    //data to a golbal service

    //删除result table  
    this.searchResultMessageService.serachResultisShow = false
    // show detail card
    this.searchResultMessageService.detailCardisShow = true
  }

  checkvalue(x: any) {
    if (x !== null && typeof x !== "undefined" && x !== "Undefined") {
      return true
    }
    return false
  }

}
