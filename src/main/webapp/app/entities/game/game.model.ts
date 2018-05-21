import { BaseEntity } from './../../shared';

export class Game implements BaseEntity {
    constructor(
        public id?: number,
        public matchTime?: any,
        public goalsTeam1?: number,
        public goalsTeam2?: number,
        public team1?: BaseEntity,
        public team2?: BaseEntity,
    ) {
    }
}
