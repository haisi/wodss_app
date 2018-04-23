import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import {Subject} from "rxjs/Subject";
import 'rxjs/add/operator/debounceTime';

import {Team, TeamService} from "../team";
import {BetTeam, BetTeamService} from "../bet-team";


@Component({
    selector: 'ranking',
    templateUrl: './ranking.component.html'
})
export class RankingComponent implements OnInit, OnDestroy {

    currentAccount: any;
    users: User[];
    betTeams: BetTeam[];
    betTeam: BetTeam;
    error: any;
    success: any;
    routeData: any;
    links: any;
    linksTeam: any;
    totalItems: any;
    totalItemsTeam: any;
    queryCount: any;
    queryCountTeam: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    predicateT: any;
    previousPage: any;
    reverse: any;
    reverseT: any;
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
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.reverseT = data['pagingParams'].ascending;
            this.predicateT = data['pagingParams'].predicate;
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
        this.router.onSameUrlNavigation = 'reload';
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
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sortTeam() }).subscribe(
            (res: HttpResponse<BetTeam[]>) => this.onSuccessTeam(res.body, res.headers),
            (res: HttpResponse<any>) => this.onError(res.body)
        );
    }



    trackIdentity(index, item: User) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
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

    transitionTeam(this, betTeam) {
        this.router.navigate(['/ranking'], {
            queryParams: {
                page: this.page
                /*sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')*/
            }
        });
        this.sortMembersOfTeam( betTeam);
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data;
    }

    private onSuccessTeam(data, headers) {
        this.linksTeam = this.parseLinks.parse(headers.get('link'));
        this.totalItemsTeam = headers.get('X-Total-Count');
        this.queryCountTeam = this.totalItemsTeam;
        this.betTeams = data;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    sortMembersOfTeam( betTeam ){

        const index = this.betTeams.indexOf(betTeam)

        if (this.predicate == 'login')
            this.betTeams[index].members.sort().reverse();
         else if (this.predicate == 'rank')
            this.betTeam.members.sort().reverse();
        else
            this.betTeams[index].members.sort((a, b) => (a.points - b.points));

    }

}
