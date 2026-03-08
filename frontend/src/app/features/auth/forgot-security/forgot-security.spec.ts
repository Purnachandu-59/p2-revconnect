import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotSecurity } from './forgot-security';

describe('ForgotSecurity', () => {
  let component: ForgotSecurity;
  let fixture: ComponentFixture<ForgotSecurity>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgotSecurity]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgotSecurity);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
