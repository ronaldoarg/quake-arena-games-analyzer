import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardComponent } from './card.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';

@NgModule({
  declarations: [CardComponent],
  imports: [
    CommonModule,
    MatExpansionModule,
    MatTableModule,
    MatChipsModule,
    MatDividerModule,
    MatListModule
  ],
  exports: [CardComponent],
})
export class CardModule { }
