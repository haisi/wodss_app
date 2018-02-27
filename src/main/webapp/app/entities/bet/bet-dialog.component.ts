import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Bet } from './bet.model';
import { BetPopupService } from './bet-popup.service';
import { BetService } from './bet.service';
import { User, UserService } from '../../shared';
import { Game, GameService } from '../game';

@Component({
    selector: 'jhi-bet-dialog',
    templateUrl: './bet-dialog.component.html'
})
export class BetDialogComponent implements OnInit {

    bet: Bet;
    isSaving: boolean;

    users: User[];

    games: Game[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private betService: BetService,
        private userService: UserService,
        private gameService: GameService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.gameService.query()
            .subscribe((res: HttpResponse<Game[]>) => { this.games = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bet.id !== undefined) {
            this.subscribeToSaveResponse(
                this.betService.update(this.bet));
        } else {
            this.subscribeToSaveResponse(
                this.betService.create(this.bet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Bet>>) {
        result.subscribe((res: HttpResponse<Bet>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Bet) {
        this.eventManager.broadcast({ name: 'betListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackGameById(index: number, item: Game) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bet-popup',
    template: ''
})
export class BetPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private betPopupService: BetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.betPopupService
                    .open(BetDialogComponent as Component, params['id']);
            } else {
                this.betPopupService
                    .open(BetDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
