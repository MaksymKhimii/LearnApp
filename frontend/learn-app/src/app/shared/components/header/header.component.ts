import {Component, ElementRef, HostListener, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  username: string | null = ''
  isMiniProfileOVisible: boolean = false

  constructor(private userService: UserService,
              private elementRef: ElementRef) {
  }

  ngOnInit(): void {
    this.username = this.userService.getUsername()
  }

  isAuthenticated() {
    const username = this.userService.getUsername()
    return !(username == null || username === '');
  }

  openMiniProfile() {
    this.isMiniProfileOVisible = true
  }

  /**
   * method for listaning flag from other component
   * in order to hide mini profile
   * after clicking on other objects
   * @param event
   */
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const clickedInside = this.elementRef.nativeElement.contains(event.target);
    if (!clickedInside && this.isMiniProfileOVisible) {
      this.isMiniProfileOVisible = false;
    }
  }
}
