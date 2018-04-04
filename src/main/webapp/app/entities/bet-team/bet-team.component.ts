import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { BetTeam } from './bet-team.model';
import { BetTeamService } from './bet-team.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-bet-team',
    templateUrl: './bet-team.component.html'
})
export class BetTeamComponent implements OnInit, OnDestroy {

currentAccount: any;
    betTeams: BetTeam[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private betTeamService: BetTeamService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.betTeamService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<BetTeam[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/bet-team'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/bet-team', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInBetTeams();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: BetTeam) {
        return item.id;
    }
    registerChangeInBetTeams() {
        this.eventSubscriber = this.eventManager.subscribe('betTeamListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    /**
     * Checks whether the logged in user is a member of the passed betTeam
     *
     * @param {BetTeam} betTeam
     * @returns {boolean} true if user is member of the betTeam
     */
    isMember(betTeam: BetTeam) {
        for (const member of betTeam.members) {
            if (member.id === this.currentAccount.id) {
                return true;
            }
        }
        return false;
    }

    join(betTeamId: number) {
        // TODO: fix error handling
        this.betTeamService.join(betTeamId)
            .subscribe((res: HttpResponse<BetTeam>) => this.clear(), (res: HttpErrorResponse) => console.log('Failed to join'));
    }

    leave(betTeamId: number) {
        // TODO: fix error handling
        this.betTeamService.leave(betTeamId)
            .subscribe((res: HttpResponse<BetTeam>) => this.clear(), (res: HttpErrorResponse) => console.log('Failed to leave'));
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.betTeams = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
