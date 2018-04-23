import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetChampionSharedModule } from '../../shared';
import {InlineEditorModule} from '@qontu/ngx-inline-editor';
import {
    GameService,
    GamePopupService,
    GameComponent,
    GameDetailComponent,
    GameDialogComponent,
    GamePopupComponent,
    GameDeletePopupComponent,
    GameDeleteDialogComponent,
    gameRoute,
    gamePopupRoute,
    GameResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...gameRoute,
    ...gamePopupRoute,
];

@NgModule({
    imports: [
        BetChampionSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        InlineEditorModule
    ],
    declarations: [
        GameComponent,
        GameDetailComponent,
        GameDialogComponent,
        GameDeleteDialogComponent,
        GamePopupComponent,
        GameDeletePopupComponent,
    ],
    entryComponents: [
        GameComponent,
        GameDialogComponent,
        GamePopupComponent,
        GameDeleteDialogComponent,
        GameDeletePopupComponent,
    ],
    providers: [
        GameService,
        GamePopupService,
        GameResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetChampionGameModule {}
