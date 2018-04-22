import { BaseEntity } from './../../shared';

export class Game implements BaseEntity {
    constructor(
        public id?: number,
        public matchTime?: any,
        public goalsTeam1?: number,
        public goalsTeam2?: number,
        public team1?: BaseEntity,
        public team2?: BaseEntity,
        public totalBets?: number,
        public team1Won?: number,
        public team2Won?: number,
        public draw?: number,

    ) {
    }
}
