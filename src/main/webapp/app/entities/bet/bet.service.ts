import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Bet } from './bet.model';
import { createRequestOption } from '../../shared';
import {Betgame} from "./betgame.model";

export type EntityResponseType = HttpResponse<Bet>;

@Injectable()
export class BetService {

    private resourceUrl =  SERVER_API_URL + 'api/bets';

    constructor(private http: HttpClient) { }

    upsert(betgame: Betgame): Observable<EntityResponseType> {
        const copy = Object.assign({}, betgame);

        return this.http.post<Bet>('api/upsert', copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    create(bet: Bet): Observable<EntityResponseType> {
        const copy = this.convert(bet);
        return this.http.post<Bet>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(bet: Bet): Observable<EntityResponseType> {
        const copy = this.convert(bet);
        return this.http.put<Bet>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Bet>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    getAllBetsAndGames(): Observable<HttpResponse<Betgame[]>> {
        return this.http.get<Betgame[]>('api/mybets', { observe: 'response' })
            .map((res: HttpResponse<Betgame[]>) => this.convertBetgameArrayResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Bet[]>> {
        const options = createRequestOption(req);
        return this.http.get<Bet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Bet[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Bet = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Bet[]>): HttpResponse<Bet[]> {
        const jsonResponse: Bet[] = res.body;
        const body: Bet[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertBetgameArrayResponse(res: HttpResponse<Betgame[]>): HttpResponse<Betgame[]> {
        const jsonResponse: Betgame[] = res.body;
        const body: Betgame[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertBetgameItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }


    /**
     * Convert a returned JSON object to Bet.
     */
    private convertItemFromServer(bet: Bet): Bet {
        const copy: Bet = Object.assign({}, bet);
        return copy;
    }

    /**
     * Convert a returned JSON object to Bet.
     */
    private convertBetgameItemFromServer(bet: Betgame): Betgame {
        const copy: Betgame = Object.assign({}, bet);
        return copy;
    }


    /**
     * Convert a Bet to a JSON which can be sent to the server.
     */
    private convert(bet: Bet): Bet {
        const copy: Bet = Object.assign({}, bet);
        return copy;
    }
}
