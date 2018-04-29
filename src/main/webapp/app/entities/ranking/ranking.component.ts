import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import {Subject} from "rxjs/Subject";
import 'rxjs/add/operator/debounceTime';

import {BetTeam, BetTeamService} from "../bet-team";


@Component({
    selector: 'ranking',
    templateUrl: './ranking.component.html'
})
export class RankingComponent implements OnInit, OnDestroy {

    currentAccount: any;
    users: User[];
    betTeams: BetTeam[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    linksTeam: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    public searchString: string;
    private subject: Subject<string> = new Subject();

    constructor(
        private userService: UserService,
        private betTeamService: BetTeamService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = 10 //ITEMS_PER_PAGE;
        this.page = 1;
        this.predicate = 'rank';
        this.reverse = true;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInUsers();
        });
        this.subject.debounceTime(500).subscribe(() => {
            this.loadAll();
        });
        // this.router.onSameUrlNavigation = 'reload';
    }

    onKeyUp(){
        this.subject.next();
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    registerChangeInUsers() {
        this.eventManager.subscribe('userListModification', (response) => this.loadAll());
    }

    setActive(user, isActivated) {
        user.activated = isActivated;

        this.userService.update(user).subscribe(
            (response) => {
                if (response.status === 200) {
                    this.error = null;
                    this.success = 'OK';
                    this.loadAll();
                } else {
                    this.success = null;
                    this.error = 'ERROR';
                }
            });
    }

    loadAll() {
        this.userService.queryRank({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()},this.searchString).subscribe(
                    (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpResponse<any>) => this.onError(res.body)
            );

        this.betTeamService.query({
            page: 0,
            size: 99,
            sort: this.sortTeam() }).subscribe(
            (res: HttpResponse<BetTeam[]>) => this.onSuccessTeam(res.body, res.headers),
            (res: HttpResponse<any>) => this.onError(res.body)
        );
    }



    trackIdentity(index, item: User) {
        return item.id;
    }

    sort() {
        if (this.predicate === 'id')
            this.predicate = 'rank'
        let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        /*if (this.predicate !== 'id') {
            result.push('id');
        }*/
        return result;
    }

    sortTeam(){
        return ['betTeamName','asc'];
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/ranking'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data;
    }

    private onSuccessTeam(data, headers) {
        this.linksTeam = this.parseLinks.parse(headers.get('link'));
        //this.totalItemsTeam = headers.get('X-Total-Count');
        //this.queryCountTeam = this.totalItemsTeam;
        this.betTeams = data;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
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

}
