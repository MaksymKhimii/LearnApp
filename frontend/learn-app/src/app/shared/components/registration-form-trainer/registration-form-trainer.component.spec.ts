import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationFormTrainerComponent } from './registration-form-trainer.component';

describe('RegistrationFormTrainerComponent', () => {
  let component: RegistrationFormTrainerComponent;
  let fixture: ComponentFixture<RegistrationFormTrainerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationFormTrainerComponent]
    });
    fixture = TestBed.createComponent(RegistrationFormTrainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
