import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {

  ngOnInit(): void {
    this.getFavorites();
    this.isContainFavorites = (this.favorites.length > 0) ? true : false;
    console.log(this.favorites)
  }
  isContainFavorites: boolean = false
  favorites: any
  getFavorites() {
    let fs = []
    for (let i = 0; i < localStorage.length; i++) {
      let key = localStorage.key(i)
      if (key !== null) {
        let item = localStorage.getItem(key)
        if (item != null) {
          if (item.includes("Event")) {
            fs.push({ "key": key, "data": JSON.parse(item) })
          }
        }
      }
    }
    this.favorites = fs
  }
  removeEventInLocalStorage(key: string) {
    localStorage.removeItem(key)
    alert("Removed from favorites!")
    this.ngOnInit()
  }


}
