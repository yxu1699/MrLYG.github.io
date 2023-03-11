import axios from 'axios';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
//app.component.ts
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';

@Component({
  selector: 'app-searchform',
  templateUrl: './searchform.component.html',
  styleUrls: ['./searchform.component.css']
})
export class SearchformComponent implements OnInit{

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

  constructor(private route: ActivatedRoute,private http: HttpClient) {
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
