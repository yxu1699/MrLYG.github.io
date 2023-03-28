import { Component } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';
// import {Component} from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { ProgressSpinnerMode } from '@angular/material/progress-spinner';
import { MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { ModalComponent } from './../modal/modal.component';
@Component({
	selector: 'app-detail',
	templateUrl: './detail.component.html',
	styleUrls: ['./detail.component.css'],
})
export class DetailComponent {
	//spinner
	color: ThemePalette = 'primary';
	mode: ProgressSpinnerMode = 'determinate';

	detail: any

	modalRef: MdbModalRef<ModalComponent> | null = null;
	constructor(public searchResultMessageService: SearchResultMessageService, private modalService: MdbModalService) {

	}

	back() {
		this.searchResultMessageService.serachResultisShow = true
		// disable details card
		this.searchResultMessageService.detailCardisShow = false
		this.searchResultMessageService.detailCard = null
	}


	isOHCollapsed = true;
	isGRCollapsed = true;
	isCRCollapsed = true;

	toggleOHCollapse() {
		this.isOHCollapsed = !this.isOHCollapsed;
		console.log("this.isOHCollapsed", this.isOHCollapsed)
	}
	toggleGRCollapse() {
		this.isGRCollapsed = !this.isGRCollapsed;
		console.log("this.isGRCollapsed", this.isGRCollapsed)
	}
	toggleCRCollapse() {
		this.isCRCollapsed = !this.isCRCollapsed;
		console.log("this.isCRCollapsed", this.isCRCollapsed)
	}



	openModal(lati: number, long: number) {
		console.log("lat", typeof (lati), lati)
		console.log("long", typeof (long), long)

		// this.modalRef = this.modalService.open(ModalComponent)
		this.modalRef = this.modalService.open(ModalComponent, {
			data: { lat: lati, lng: long }
		})
	}


	getItemFromLocalStorage(key: string): boolean {
		let favoritesArraystring = localStorage.getItem("favoritesArray") //get list of favorites
		let favoritesArray = favoritesArraystring ? JSON.parse(favoritesArraystring) : []
		for (let i = 0; i < favoritesArray.length; i++) {
			if (favoritesArray[i].key === key) {
				return true
			}
		}
		return false
		// return !!localStorage.getItem(key);
	}
	addFavorite(key: string) {
		let date = this.searchResultMessageService.detailCard.eventdetail.data.localDate
		let favoritesArraystring = localStorage.getItem("favoritesArray") //get list of favorites
		let favoritesArray = favoritesArraystring ? JSON.parse(favoritesArraystring) : []

		let event = {
			"Date": date,
			"Event": this.searchResultMessageService.detailCard.eventdetail.data.eventname,
			"Category": this.searchResultMessageService.detailCard.eventdetail.data.genres,
			"Venue": this.searchResultMessageService.detailCard.eventdetail.data.venue,
		}
		favoritesArray.push(
			{
				"key": key,
				"data": event
			}
		)
		localStorage.setItem("favoritesArray", JSON.stringify(favoritesArray))
		alert('Event Added to Favorites!')
	}

	deleteFavorite(key: string) {
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
		alert('Event Removed from Favorites!')
	}
}
