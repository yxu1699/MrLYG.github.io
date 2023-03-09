import { Component } from '@angular/core';
import axios from 'axios';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-searchform',
  templateUrl: './searchform.component.html',
  styleUrls: ['./searchform.component.css']
})
export class SearchformComponent {

  keyword: string = "";
  distance: string = "";
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

  constructor(private route: ActivatedRoute) {
    this.category = this.availableCategory[0];
  }


  onClick(): void {
    console.log(this.keyword)
  }

  

  // control loction input text
  locationCheckBoxChange(){
    console.log(this.isAutoFindLocation)
    this.isAutoFindLocation = !this.isAutoFindLocation
  }













  submitForSearch(form: NgForm) {
    
    // event.preventDefault()
    axios.get('/api/data', {
      params: {
        keyword: this.keyword,
        distance: this.distance,
        category: this.category,
        location: this.location,
      }
    })
      .then(response => {
        console.log(response.data);
      })
      .catch(error => {
        console.log(error);
      });
  }

}
