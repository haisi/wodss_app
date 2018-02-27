import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { BetTeam } from './bet-team.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BetTeam>;

@Injectable()
export class BetTeamService {

    private resourceUrl =  SERVER_API_URL + 'api/bet-teams';

    constructor(private http: HttpClient) { }

    create(betTeam: BetTeam): Observable<EntityResponseType> {
        const copy = this.convert(betTeam);
        return this.http.post<BetTeam>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(betTeam: BetTeam): Observable<EntityResponseType> {
        const copy = this.convert(betTeam);
        return this.http.put<BetTeam>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BetTeam>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BetTeam[]>> {
        const options = createRequestOption(req);
        return this.http.get<BetTeam[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BetTeam[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BetTeam = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BetTeam[]>): HttpResponse<BetTeam[]> {
        const jsonResponse: BetTeam[] = res.body;
        const body: BetTeam[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BetTeam.
     */
    private convertItemFromServer(betTeam: BetTeam): BetTeam {
        const copy: BetTeam = Object.assign({}, betTeam);
        return copy;
    }

    /**
     * Convert a BetTeam to a JSON which can be sent to the server.
     */
    private convert(betTeam: BetTeam): BetTeam {
        const copy: BetTeam = Object.assign({}, betTeam);
        return copy;
    }
}
