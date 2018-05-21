import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Game } from './game.model';
import { GamePopupService } from './game-popup.service';
import { GameService } from './game.service';
import { Team, TeamService } from '../team';

@Component({
    selector: 'jhi-game-dialog',
    templateUrl: './game-dialog.component.html'
})
export class GameDialogComponent implements OnInit {

    game: Game;
    isSaving: boolean;

    teams: Team[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private gameService: GameService,
        private teamService: TeamService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        const options = {page: 0, size: 40, sort: ['teamName,asc']};
        this.teamService.query(options)
            .subscribe((res: HttpResponse<Team[]>) => { this.teams = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.game.id !== undefined) {
            this.subscribeToSaveResponse(
                this.gameService.update(this.game));
        } else {
            this.subscribeToSaveResponse(
                this.gameService.create(this.game));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Game>>) {
        result.subscribe((res: HttpResponse<Game>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Game) {
        this.eventManager.broadcast({ name: 'gameListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTeamById(index: number, item: Team) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-game-popup',
    template: ''
})
export class GamePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gamePopupService: GamePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.gamePopupService
                    .open(GameDialogComponent as Component, params['id']);
            } else {
                this.gamePopupService
                    .open(GameDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
