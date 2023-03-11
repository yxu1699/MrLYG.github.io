
//app.component.ts
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';

@Component({
  selector: 'app-autocom',
  templateUrl: './autocom.component.html',
  styleUrls: ['./autocom.component.css']
})
export class AutocomComponent implements OnInit {

  suggestCtrl = new FormControl();
  filteredSuggestes: any;
  isLoading = false;
  errorMsg!: string;
  minLengthTerm = 3;
  keyword: any = "";

  constructor(
    private http: HttpClient
  ) { }

  onSelected() {
    console.log("selected")
    console.log(this.keyword);
    this.keyword = this.keyword;
  }

  displayWith(value: any) {
    return value?.name;
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
        debounceTime(1000),
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