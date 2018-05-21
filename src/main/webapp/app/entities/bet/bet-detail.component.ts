import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Bet } from './bet.model';
import { BetService } from './bet.service';

@Component({
    selector: 'jhi-bet-detail',
    templateUrl: './bet-detail.component.html'
})
export class BetDetailComponent implements OnInit, OnDestroy {

    bet: Bet;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private betService: BetService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBets();
    }

    load(id) {
        this.betService.find(id)
            .subscribe((betResponse: HttpResponse<Bet>) => {
                this.bet = betResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBets() {
        this.eventSubscriber = this.eventManager.subscribe(
            'betListModification',
            (response) => this.load(this.bet.id)
        );
    }
}
