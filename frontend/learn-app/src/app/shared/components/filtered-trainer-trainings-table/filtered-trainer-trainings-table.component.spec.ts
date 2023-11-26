import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilteredTrainerTrainingsTableComponent } from './filtered-trainer-trainings-table.component';

describe('FilteredTrainerTrainingsTableComponent', () => {
  let component: FilteredTrainerTrainingsTableComponent;
  let fixture: ComponentFixture<FilteredTrainerTrainingsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilteredTrainerTrainingsTableComponent]
    });
    fixture = TestBed.createComponent(FilteredTrainerTrainingsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
