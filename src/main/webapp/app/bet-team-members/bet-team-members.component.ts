import {Component, Input, OnInit} from '@angular/core';
import {BetTeam, BetTeamService} from "../entities/bet-team";
import {HttpResponse} from "@angular/common/http";
import {User} from "../shared";


@Component({
  selector: 'bet-team-members',
  templateUrl: './bet-team-members.component.html',
  styles: []
})
export class BetTeamMembersComponent implements OnInit {
    @Input()
    private betTeamId: number;
    private betTeam: BetTeam;
    private sortorder: any;
    private ranking: any;
    itemsPerPage: any;
    page: any;
    page2: any;
    tempMembers: User[];

    constructor(private betTeamService: BetTeamService) {
        this.sortorder = true;
        this.ranking = 'rank';
        this.itemsPerPage = 3;
        this.page = 1;
    }

    ngOnInit() {
        this.getTeam();
    }

    getTeam(){
        this.betTeamService.find(this.betTeamId).subscribe(
            (res: HttpResponse<BetTeam>) => this.onSuccess(res.body, res.headers),
            error1 => console.log(error1),
        )

    }

    onSuccess(data, headers){
        this.betTeam = data;
        this.tempMembers = this.betTeam.members.slice(0,3);
    }

    transition() {
        if (this.ranking === 'points' && this.sortorder) {
            this.tempMembers = this.tempMembers.sort((a, b) => {
                if (a.points < b.points) {
                    return -1;
                }
                if (a.points > b.points) {
                    return 1;
                }
                // a must be equal to b
                return 0;
            });
        }
        else if (this.ranking === 'points' && !this.sortorder) {
            this.tempMembers = this.tempMembers.sort((a, b) => {
                if (a.points > b.points) {
                    return -1;
                }
                if (a.points < b.points) {
                    return 1;
                }
                // a must be equal to b
                return 0;
            });
        }
        else if (this.ranking === 'rank' && this.sortorder) {
            this.tempMembers = this.tempMembers.sort((a, b) => {
                if (a.rank < b.rank) {
                    return -1;
                }
                if (a.rank > b.rank) {
                    return 1;
                }
                // a must be equal to b
                return 0;
            });
        }
        else if (this.ranking === 'rank' && !this.sortorder) {
            this.tempMembers = this.tempMembers.sort((a, b) => {
                if (a.rank > b.rank) {
                    return -1;
                }
                if (a.rank < b.rank) {
                    return 1;
                }
                // a must be equal to b
                return 0;
            });
        }
        else if (this.ranking === 'login' && this.sortorder) {
            this.tempMembers = this.tempMembers.sort((a, b) => {
                if (a.login < b.login) {
                    return -1;
                }
                if (a.login > b.login) {
                    return 1;
                }
                // a must be equal to b
                return 0;
            });
        }
        else {
            this.tempMembers = this.tempMembers.sort((a, b) => {
                if (a.login > b.login) {
                    return -1;
                }
                if (a.login < b.login) {
                    return 1;
                }
                // a must be equal to b
                return 0;
            });
        }
    }

    loadPage(page){
        console.log(page);
        this.page = page;
        this.tempMembers = this.betTeam.members.slice((this.page-1)*3, (this.page-1)*3+3);
    }
}
