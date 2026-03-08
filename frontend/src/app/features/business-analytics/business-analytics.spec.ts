import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessAnalytics } from './business-analytics';

describe('BusinessAnalytics', () => {
  let component: BusinessAnalytics;
  let fixture: ComponentFixture<BusinessAnalytics>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BusinessAnalytics]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BusinessAnalytics);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
