import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BetComponent } from './bet.component';
import { BetDetailComponent } from './bet-detail.component';
import { BetPopupComponent } from './bet-dialog.component';
import { BetDeletePopupComponent } from './bet-delete-dialog.component';

@Injectable()
export class BetResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const betRoute: Routes = [
    {
        path: 'bet',
        component: BetComponent,
        resolve: {
            'pagingParams': BetResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.bet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bet/:id',
        component: BetDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.bet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const betPopupRoute: Routes = [
    {
        path: 'bet-new',
        component: BetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.bet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bet/:id/edit',
        component: BetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.bet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bet/:id/delete',
        component: BetDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.bet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
