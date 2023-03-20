import { Component } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';
@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css'],
})
export class DetailComponent {
	constructor(public searchResultMessageService:SearchResultMessageService){
    
	}

	back(){
		this.searchResultMessageService.serachResultisShow = true
		// disable details card
		this.searchResultMessageService.detailCardisShow = false
		this.searchResultMessageService.detailCard = null
	}
}
