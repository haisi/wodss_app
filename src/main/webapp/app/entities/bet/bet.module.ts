import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetChampionSharedModule } from '../../shared';
import { BetChampionAdminModule } from '../../admin/admin.module';
import {InlineEditorModule} from '@qontu/ngx-inline-editor';
import {
    BetService,
    BetPopupService,
    BetComponent,
    BetDetailComponent,
    BetDialogComponent,
    BetPopupComponent,
    BetDeletePopupComponent,
    BetDeleteDialogComponent,
    betRoute,
    betPopupRoute,
    BetResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...betRoute,
    ...betPopupRoute,
];

@NgModule({
    imports: [
        BetChampionSharedModule,
        BetChampionAdminModule,
        RouterModule.forChild(ENTITY_STATES),
        InlineEditorModule
    ],
    declarations: [
        BetComponent,
        BetDetailComponent,
        BetDialogComponent,
        BetDeleteDialogComponent,
        BetPopupComponent,
        BetDeletePopupComponent,
    ],
    entryComponents: [
        BetComponent,
        BetDialogComponent,
        BetPopupComponent,
        BetDeleteDialogComponent,
        BetDeletePopupComponent,
    ],
    providers: [
        BetService,
        BetPopupService,
        BetResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetChampionBetModule {}
