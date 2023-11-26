import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './shared/components/login-form/login-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { RegistrationFormTrainerComponent } from './shared/components/registration-form-trainer/registration-form-trainer.component';
import { RegistrationFormTraineeComponent } from './shared/components/registration-form-trainee/registration-form-trainee.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { HeaderComponent } from './shared/components/header/header.component';
import { RegisterAsComponent } from './shared/components/register-as/register-as.component';
import { ButtonComponent } from './shared/components/button/button.component';
import { BoxComponent } from './shared/components/box/box.component';
import { AlertComponent } from './shared/components/alert/alert.component';
import { ModalBoxComponent } from './shared/components/modal-box/modal-box.component';
import { MiniProfileComponent } from './shared/components/mini-profile/mini-profile.component';
import { DatePickerComponent } from './shared/components/date-picker/date-picker.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TableComponent } from './shared/components/table/table.component';
import { MyAccountComponent } from './shared/components/my-account/my-account.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ChangePasswordPageComponent } from './pages/change-password-page/change-password-page.component';
import { ChangePasswordFormComponent } from './shared/components/change-password-form/change-password-form.component';
import { JoinUsPageComponent } from './pages/join-us-page/join-us-page.component';
import { RegistrationPageComponent } from './pages/registration-page/registration-page.component';
import { MyAccountPageComponent } from './pages/my-account-page/my-account-page.component';
import { TrainersTableComponent } from './shared/components/trainers-table/trainers-table.component';
import { TrainingsPageComponent } from './pages/trainings-page/trainings-page.component';
import {HttpClientModule} from "@angular/common/http";
import { StudentsTableComponent } from './shared/components/students-table/students-table.component';
import { AddTrainingPageComponent } from './pages/add-training-page/add-training-page.component';
import { FilteredTrainerTrainingsTableComponent } from './shared/components/filtered-trainer-trainings-table/filtered-trainer-trainings-table.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    RegistrationFormTrainerComponent,
    RegistrationFormTraineeComponent,
    FooterComponent,
    HeaderComponent,
    RegisterAsComponent,
    ButtonComponent,
    BoxComponent,
    AlertComponent,
    ModalBoxComponent,
    MiniProfileComponent,
    DatePickerComponent,
    TableComponent,
    MyAccountComponent,
    LoginPageComponent,
    HomePageComponent,
    ChangePasswordPageComponent,
    ChangePasswordFormComponent,
    JoinUsPageComponent,
    RegistrationPageComponent,
    MyAccountPageComponent,
    TrainersTableComponent,
    TrainingsPageComponent,
    StudentsTableComponent,
    AddTrainingPageComponent,
    FilteredTrainerTrainingsTableComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    BsDatepickerModule.forRoot(),
    NoopAnimationsModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
