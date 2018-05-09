import { BaseEntity } from './../../shared';

export class Betgame implements BaseEntity {
    constructor(
        public id?: number,
        public gameId?: number,
        public matchTime?: Date,
        public gameClosed?: boolean,
        public team1Id?: number,
        public team1Name?: string,
        public team2Id?: number,
        public team2Name?: string,
        public goalsTeam1?: number,
        public goalsTeam2?: number,

        public betId?: number,
        public userId?: number,
        public betGoalTeam1?: number,
        public betGoalTeam2?: number,
        public stats?: any,
    ) {
    }
}
