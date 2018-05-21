import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {
    BetChampionSharedLibsModule,
    BetChampionSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    JhiLoginModalComponent,
    Principal,
    HasAnyAuthorityDirective,
} from './';
import {FilterPipe} from '../pipes';

@NgModule({
    imports: [
        BetChampionSharedLibsModule,
        BetChampionSharedCommonModule
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        FilterPipe
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        UserService,
        DatePipe
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        BetChampionSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe,
        FilterPipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class BetChampionSharedModule {}
