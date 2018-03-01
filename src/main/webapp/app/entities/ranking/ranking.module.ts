import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetChampionSharedModule } from '../../shared';
import { BetChampionAdminModule } from '../../admin/admin.module';

import {RankingComponent} from "./ranking.component";
import {RankingResolvePagingParams, rankingRoute} from "./ranking.route";
import {UserModalService} from "../../admin";

const ENTITY_STATES = [
    ...rankingRoute
];

@NgModule({
    imports: [
        BetChampionSharedModule,
        BetChampionAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RankingComponent
    ],
    entryComponents: [
        RankingComponent
    ],
    providers: [
        UserModalService,
        RankingResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetChampionRankingModule {}
