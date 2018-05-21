import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { BetTeam } from './bet-team.model';
import { BetTeamService } from './bet-team.service';

@Component({
    selector: 'jhi-bet-team-detail',
    templateUrl: './bet-team-detail.component.html'
})
export class BetTeamDetailComponent implements OnInit, OnDestroy {

    betTeam: BetTeam;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    public searchString: string;
    constructor(
        private eventManager: JhiEventManager,
        private betTeamService: BetTeamService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBetTeams();
    }

    load(id) {
        this.betTeamService.find(id)
            .subscribe((betTeamResponse: HttpResponse<BetTeam>) => {
                this.betTeam = betTeamResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBetTeams() {
        this.eventSubscriber = this.eventManager.subscribe(
            'betTeamListModification',
            (response) => this.load(this.betTeam.id)
        );
    }
}
