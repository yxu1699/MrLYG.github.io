import { TestBed } from '@angular/core/testing';

import { SearchResultMessageService } from './search-result-message.service';

describe('SearchResultMessageService', () => {
  let service: SearchResultMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchResultMessageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
