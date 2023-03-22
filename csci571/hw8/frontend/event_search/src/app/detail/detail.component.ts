import { Component } from '@angular/core';
import { SearchResultMessageService } from '../search-result-message.service';
// import {Component} from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { ProgressSpinnerMode } from '@angular/material/progress-spinner';
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
	constructor(public searchResultMessageService: SearchResultMessageService) {

	}

	back() {
		this.searchResultMessageService.serachResultisShow = true
		// disable details card
		this.searchResultMessageService.detailCardisShow = false
		this.searchResultMessageService.detailCard = null
	}
}
