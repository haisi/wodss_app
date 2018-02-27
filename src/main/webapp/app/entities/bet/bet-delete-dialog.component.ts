import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Bet } from './bet.model';
import { BetPopupService } from './bet-popup.service';
import { BetService } from './bet.service';

@Component({
    selector: 'jhi-bet-delete-dialog',
    templateUrl: './bet-delete-dialog.component.html'
})
export class BetDeleteDialogComponent {

    bet: Bet;

    constructor(
        private betService: BetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.betService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'betListModification',
                content: 'Deleted an bet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bet-delete-popup',
    template: ''
})
export class BetDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private betPopupService: BetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.betPopupService
                .open(BetDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
