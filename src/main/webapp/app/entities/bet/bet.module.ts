import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetChampionSharedModule } from '../../shared';
import { BetChampionAdminModule } from '../../admin/admin.module';
import {InlineEditorModule} from '@qontu/ngx-inline-editor';
import {
    BetService,
    BetComponent,
    betRoute,
    BetResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...betRoute,
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
    ],
    entryComponents: [
        BetComponent,
    ],
    providers: [
        BetService,
        BetResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetChampionBetModule {}
