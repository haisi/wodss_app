import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BetTeam } from './bet-team.model';
import { BetTeamPopupService } from './bet-team-popup.service';
import { BetTeamService } from './bet-team.service';

@Component({
    selector: 'jhi-bet-team-delete-dialog',
    templateUrl: './bet-team-delete-dialog.component.html'
})
export class BetTeamDeleteDialogComponent {

    betTeam: BetTeam;

    constructor(
        private betTeamService: BetTeamService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.betTeamService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'betTeamListModification',
                content: 'Deleted an betTeam'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bet-team-delete-popup',
    template: ''
})
export class BetTeamDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private betTeamPopupService: BetTeamPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.betTeamPopupService
                .open(BetTeamDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
