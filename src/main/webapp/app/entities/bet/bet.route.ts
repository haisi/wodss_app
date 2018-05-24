import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BetComponent } from './bet.component';

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
    }
];
