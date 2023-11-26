import {Component, Input, OnInit} from '@angular/core';
import {ThemeService} from "../../services/theme.service";
import {UserService} from "../../services/user.service";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-mini-profile',
  templateUrl: './mini-profile.component.html',
  styleUrls: ['./mini-profile.component.scss']
})
export class MiniProfileComponent implements OnInit {
  @Input() username: string | null = 'New User'

  isDarkMode: boolean;

  constructor(private router: Router,
              private userService: UserService,
              private authService: AuthService,
              private themeService: ThemeService) {
    this.isDarkMode = this.themeService.isDarkMode();
  }

  ngOnInit(): void {
    this.username = this.userService.getUsername()
  }

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
    this.themeService.setDarkMode(this.isDarkMode);
  }

  logoutUser() {
    this.authService.logout()
    this.router.navigate(['/'])
  }
}
