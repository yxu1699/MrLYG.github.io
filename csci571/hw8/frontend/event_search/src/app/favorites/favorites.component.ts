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
    let favoritesArraystring = localStorage.getItem("favoritesArray") //get list of favorites
    let favoritesArray = favoritesArraystring ? JSON.parse(favoritesArraystring) :[]

    for (let i = 0; i < favoritesArray.length; i++) {
      let item = favoritesArray[i];
      fs.push({ "key": item.key, "data": item.data })
      
    }
    console.log("fs[]",fs)
    this.favorites = fs
  }
  removeEventInLocalStorage(key: string) {
    let favoritesArraystring = localStorage.getItem("favoritesArray") //get list of favorites
		let favoritesArray = favoritesArraystring ? JSON.parse(favoritesArraystring) : []
		let index = -1
		for (let i = 0; i < favoritesArray.length; i++) {
			if (favoritesArray[i].key === key) {
				index = i
				break
			}
		}

		if (index !== -1) {
			favoritesArray.splice(index, 1);
		}
		localStorage.setItem("favoritesArray", JSON.stringify(favoritesArray));
    alert("Removed from favorites!")
    this.ngOnInit()
  }


}
