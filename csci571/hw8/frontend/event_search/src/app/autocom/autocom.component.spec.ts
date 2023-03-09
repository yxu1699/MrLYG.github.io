import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AutocomComponent } from './autocom.component';

describe('AutocomComponent', () => {
  let component: AutocomComponent;
  let fixture: ComponentFixture<AutocomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AutocomComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AutocomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
