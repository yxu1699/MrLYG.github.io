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
		return !!localStorage.getItem(key);
	}
	addFavorite(key: string) {
		let date = this.searchResultMessageService.detailCard.eventdetail.data.localDate
		if (this.searchResultMessageService.detailCard.eventdetail.data.localTime) {
			date = date + " " + this.searchResultMessageService.detailCard.eventdetail.data.localTime
		}
		let event = {
			"Date":date,
			"Event":this.searchResultMessageService.detailCard.eventdetail.data.eventname,
			"Category":this.searchResultMessageService.detailCard.eventdetail.data.genres,
			"Venue":this.searchResultMessageService.detailCard.eventdetail.data.venue,
		}
		localStorage.setItem(key,JSON.stringify(event))
		alert('Event Added to Favorites!')
	}

	deleteFavorite(key: string){
		localStorage.removeItem(key)
		// alert('Removed from favorites!')
	}
}
