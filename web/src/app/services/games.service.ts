import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Game } from '../models/game.types';

@Injectable({
  providedIn: 'root'
})
export class GamesService {

  constructor(private httpClient: HttpClient) { }

  getAll(): Observable<Game[]> {
    return this.httpClient.get("http://localhost:8080/games")
      .pipe(map((res: any) => {
        return res.games
      }))
  }
}
