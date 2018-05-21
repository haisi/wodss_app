import { BaseEntity, User } from './../../shared';

export class Bet implements BaseEntity {
    constructor(
        public id?: number,
        public goalsTeam1?: number,
        public goalsTeam2?: number,
        public user?: User,
        public game?: BaseEntity,
    ) {
    }
}
