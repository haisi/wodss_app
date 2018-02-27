import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetChampionSharedModule } from '../../shared';
import { BetChampionAdminModule } from '../../admin/admin.module';
import {
    BetTeamService,
    BetTeamPopupService,
    BetTeamComponent,
    BetTeamDetailComponent,
    BetTeamDialogComponent,
    BetTeamPopupComponent,
    BetTeamDeletePopupComponent,
    BetTeamDeleteDialogComponent,
    betTeamRoute,
    betTeamPopupRoute,
    BetTeamResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...betTeamRoute,
    ...betTeamPopupRoute,
];

@NgModule({
    imports: [
        BetChampionSharedModule,
        BetChampionAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BetTeamComponent,
        BetTeamDetailComponent,
        BetTeamDialogComponent,
        BetTeamDeleteDialogComponent,
        BetTeamPopupComponent,
        BetTeamDeletePopupComponent,
    ],
    entryComponents: [
        BetTeamComponent,
        BetTeamDialogComponent,
        BetTeamPopupComponent,
        BetTeamDeleteDialogComponent,
        BetTeamDeletePopupComponent,
    ],
    providers: [
        BetTeamService,
        BetTeamPopupService,
        BetTeamResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetChampionBetTeamModule {}
