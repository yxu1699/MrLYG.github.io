import { Component } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';
import { TicketmarketapiService } from '../ticketmarketapi.service';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent {
  constructor(public searchResultMessageService:SearchResultMessageService, private ticketmarketapiService:TicketmarketapiService){
    
  }

  detail:any
  /*{
    eventdetail:{
      'iscontainData': true,
      'data': evens
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
  getDetail(id:any){
    console.log("getDetail",id)
    //get event--
    this.ticketmarketapiService.getEventById(id).subscribe(data => {
      console.log(data)
      
      let artistName = data._embedded.attractions[0].name
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
}
