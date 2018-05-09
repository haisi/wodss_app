import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Bet } from './bet.model';
import { BetService } from './bet.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { Game, GameService } from '../game';
import { Betgame } from './betgame.model';

@Component({
    selector: 'jhi-bet',
    templateUrl: './bet.component.html'
})
export class BetComponent implements OnInit, OnDestroy {

    currentAccount: any;
    bets: Betgame[];
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
    games: Game[];
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
        this.betService.getAllBetsAndGames().subscribe(
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
        this.router.navigate(['/bet']);
        this.loadAll();
    }

    clear() {
        this.router.navigate(['/bet']);
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

    private onSuccess(data, headers) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        this.bets = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

}
