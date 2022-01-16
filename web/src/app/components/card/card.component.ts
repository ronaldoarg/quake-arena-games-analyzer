import { Component, Input, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.types';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent {

  @Input() game!: Game;

  constructor() { }
}
