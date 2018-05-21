import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BetTeam } from './bet-team.model';
import { BetTeamPopupService } from './bet-team-popup.service';
import { BetTeamService } from './bet-team.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-bet-team-dialog',
    templateUrl: './bet-team-dialog.component.html'
})
export class BetTeamDialogComponent implements OnInit {

    betTeam: BetTeam;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private betTeamService: BetTeamService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.betTeam.id !== undefined) {
            this.subscribeToSaveResponse(
                this.betTeamService.update(this.betTeam));
        } else {
            this.subscribeToSaveResponse(
                this.betTeamService.create(this.betTeam));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BetTeam>>) {
        result.subscribe((res: HttpResponse<BetTeam>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BetTeam) {
        this.eventManager.broadcast({ name: 'betTeamListModification', content: 'OK'});
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-bet-team-popup',
    template: ''
})
export class BetTeamPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private betTeamPopupService: BetTeamPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.betTeamPopupService
                    .open(BetTeamDialogComponent as Component, params['id']);
            } else {
                this.betTeamPopupService
                    .open(BetTeamDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
