import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationFormTraineeComponent } from './registration-form-trainee.component';

describe('RegistrationFormTraineeComponent', () => {
  let component: RegistrationFormTraineeComponent;
  let fixture: ComponentFixture<RegistrationFormTraineeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationFormTraineeComponent]
    });
    fixture = TestBed.createComponent(RegistrationFormTraineeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
