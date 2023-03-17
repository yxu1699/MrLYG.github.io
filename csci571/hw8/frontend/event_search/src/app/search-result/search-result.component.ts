import { Component } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent {
  constructor(public searchResultMessageService:SearchResultMessageService){
    
  }

  //recieve id get event detail
  getDetail(id:any){
    console.log("getDetail",id)
    //data to a golbal service
    //删除result table 
    this.searchResultMessageService.serachResultisShow = false
    // show detail card
    this.searchResultMessageService.detailCardisShow = true
  }
  
}
