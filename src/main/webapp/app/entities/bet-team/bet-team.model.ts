import { BaseEntity, User } from './../../shared';

export class BetTeam implements BaseEntity {
    constructor(
        public id?: number,
        public betTeamName?: string,
        public members?: User[],
    ) {
    }
}
