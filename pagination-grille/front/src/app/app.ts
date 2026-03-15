import { Component, signal } from '@angular/core';
import {UserProjectGridComponent} from './ui/user-project-grid.component';

@Component({
  selector: 'app-root',
  imports: [
    UserProjectGridComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('front');
}
