import { BaseEntity } from './../../shared';
import {Team} from '../team';

export class Game implements BaseEntity {
    constructor(
        public id?: number,
        public matchTime?: any,
        public goalsTeam1?: number,
        public goalsTeam2?: number,
        public team1?: Team,
        public team2?: Team,
        public totalBets?: number,
        public team1Won?: number,
        public team2Won?: number,
        public draw?: number,
        public info?: string,

    ) {
    }
}
