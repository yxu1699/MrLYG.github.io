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
  
}
