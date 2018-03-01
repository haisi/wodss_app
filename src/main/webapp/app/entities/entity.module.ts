import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BetChampionTeamModule } from './team/team.module';
import { BetChampionBetModule } from './bet/bet.module';
import { BetChampionGameModule } from './game/game.module';
import { BetChampionBetTeamModule } from './bet-team/bet-team.module';
import { BetChampionRankingModule} from './ranking/ranking.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        BetChampionTeamModule,
        BetChampionBetModule,
        BetChampionGameModule,
        BetChampionBetTeamModule,
        BetChampionRankingModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetChampionEntityModule {}
