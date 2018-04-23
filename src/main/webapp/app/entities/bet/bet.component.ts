import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Bet } from './bet.model';
import { BetService } from './bet.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {Game, GameService} from "../game";

@Component({
    selector: 'jhi-bet',
    templateUrl: './bet.component.html'
})
export class BetComponent implements OnInit, OnDestroy {

    currentAccount: any;
    bets: Bet[];
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
    isCreator: boolean;
    games: Game[];
    userBets: Bet[];
    size: number;

    constructor(
        private betService: BetService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private gameService: GameService
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
        this.betService.query({
            page: this.page - 1,
            size: 999, // get it all!!!
            sort: this.sort()}).subscribe(
                (res: HttpResponse<Bet[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/bet'], {queryParams:
            {
                page: this.page,
                size: 999,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/bet', {
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
        this.registerChangeInBets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Bet) {
        return item.id;
    }
    registerChangeInBets() {
        this.eventSubscriber = this.eventManager.subscribe('betListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.bets = data;
        this.loadGames();
        this.filterBets();
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    isUser(bet: Bet) {
        if (bet.user.id === this.currentAccount.id) {
            this.isCreator = true;
            return true;
        }
        this.isCreator = false;
        return false;
    }

    loadGames(){
        this.gameService.query().subscribe(y => this.games = y.body);
    }

    filterBets(){
        this.userBets = this.bets;
        this.userBets = this.userBets.filter(x => x.user.id === this.currentAccount.id)
    }

}
