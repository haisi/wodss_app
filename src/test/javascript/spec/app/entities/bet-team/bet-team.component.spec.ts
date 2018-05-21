/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BetChampionTestModule } from '../../../test.module';
import { BetTeamComponent } from '../../../../../../main/webapp/app/entities/bet-team/bet-team.component';
import { BetTeamService } from '../../../../../../main/webapp/app/entities/bet-team/bet-team.service';
import { BetTeam } from '../../../../../../main/webapp/app/entities/bet-team/bet-team.model';

describe('Component Tests', () => {

    describe('BetTeam Management Component', () => {
        let comp: BetTeamComponent;
        let fixture: ComponentFixture<BetTeamComponent>;
        let service: BetTeamService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BetChampionTestModule],
                declarations: [BetTeamComponent],
                providers: [
                    BetTeamService
                ]
            })
            .overrideTemplate(BetTeamComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BetTeamComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BetTeamService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BetTeam(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.betTeams[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
