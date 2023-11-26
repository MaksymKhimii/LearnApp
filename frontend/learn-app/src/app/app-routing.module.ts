import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {ChangePasswordPageComponent} from "./pages/change-password-page/change-password-page.component";
import {RegistrationPageComponent} from "./pages/registration-page/registration-page.component";
import {JoinUsPageComponent} from "./pages/join-us-page/join-us-page.component";
import {MyAccountPageComponent} from "./pages/my-account-page/my-account-page.component";
import {TrainingsPageComponent} from "./pages/trainings-page/trainings-page.component";
import {AddTrainingPageComponent} from "./pages/add-training-page/add-training-page.component";

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'login', component: LoginPageComponent},
  {path: 'register/:userType', component: RegistrationPageComponent},
  {path: 'change-password', component: ChangePasswordPageComponent},
  {path: 'join', component: JoinUsPageComponent},
  {path: 'account', component: MyAccountPageComponent},
  {path: 'trainings', component: TrainingsPageComponent},
  {path: 'trainings/add', component: AddTrainingPageComponent},
  {path: '**', component: HomePageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
