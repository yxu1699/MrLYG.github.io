import { Component } from '@angular/core';

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
  onClick(): void {
    console.log(this.keyword)
  }
  constructor() {
    //Old Code
   // this.title = this.titles[0]name;

  //New Code
  this.category = this.availableCategory[0];
  }
}
