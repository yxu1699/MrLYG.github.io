import { TestBed } from '@angular/core/testing';

import { TicketmarketapiService } from './ticketmarketapi.service';

describe('TicketmarketapiService', () => {
  let service: TicketmarketapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketmarketapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
