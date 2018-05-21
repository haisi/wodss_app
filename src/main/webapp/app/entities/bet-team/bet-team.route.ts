import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BetTeamComponent } from './bet-team.component';
import { BetTeamDetailComponent } from './bet-team-detail.component';
import { BetTeamPopupComponent } from './bet-team-dialog.component';
import { BetTeamDeletePopupComponent } from './bet-team-delete-dialog.component';

@Injectable()
export class BetTeamResolvePagingParams implements Resolve<any> {

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

export const betTeamRoute: Routes = [
    {
        path: 'bet-team',
        component: BetTeamComponent,
        resolve: {
            'pagingParams': BetTeamResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.betTeam.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bet-team/:id',
        component: BetTeamDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.betTeam.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const betTeamPopupRoute: Routes = [
    {
        path: 'bet-team-new',
        component: BetTeamPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.betTeam.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bet-team/:id/edit',
        component: BetTeamPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.betTeam.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bet-team/:id/delete',
        component: BetTeamDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'betChampionApp.betTeam.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
