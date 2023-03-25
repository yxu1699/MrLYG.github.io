import { Component, Input, EventEmitter, OnInit } from '@angular/core';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  // lat: number = 37.4219999
  // lng: number = -122.0840575

  // mapOptions: google.maps.MapOptions = {
  //   zoom: 14,
  //   center: { lat: 37.4219999, lng:  -122.0840575 },
  // };

  // marker = {
  //   position: { lat: 37.4219999, lng: -122.0840575 },
  // }

  // @Input() lat: number = 37.4219999;
  // @Input() lng: number = -122.0840575;

  // mapOptions: google.maps.MapOptions = {
  //   zoom: 14,
  //   center: { lat: this.lat, lng: this.lng },
  // };

  // marker = {
  //   position: { lat: this.lat, lng: this.lng },
  // };
  // constructor(public modalRef: MdbModalRef<ModalComponent>) { }
  // ngOnInit(): void {
  //   throw new Error('Method not implemented.');
  // }
  lat: number = 37.4219999;
  lng: number = -122.0840575;

  mapOptions= {
    zoom: 14,
    center: { lat:  37.4219999, lng: -122.0840575 },
  };
  marker={
    position: {  lat:  37.4219999, lng: -122.0840575 },
  };

  constructor(public modalRef: MdbModalRef<ModalComponent>) { }

  ngOnInit() {
    this.mapOptions = {
      zoom: 14,
      center: { lat: this.lat, lng: this.lng },
    };

    this.marker = {
      position: { lat: this.lat, lng: this.lng },
    };
  }
  c(){
    console.log(this.mapOptions,this.marker)
  }
}

