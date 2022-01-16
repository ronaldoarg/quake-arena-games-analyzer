export interface Game {
    id: number,
    players: string[],
    totalKills: number,
    kills: GameKills
}

export type GameKills = {
    [key: string]: number;
};
