/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BetChampionTestModule } from '../../../test.module';
import { BetTeamDetailComponent } from '../../../../../../main/webapp/app/entities/bet-team/bet-team-detail.component';
import { BetTeamService } from '../../../../../../main/webapp/app/entities/bet-team/bet-team.service';
import { BetTeam } from '../../../../../../main/webapp/app/entities/bet-team/bet-team.model';

describe('Component Tests', () => {

    describe('BetTeam Management Detail Component', () => {
        let comp: BetTeamDetailComponent;
        let fixture: ComponentFixture<BetTeamDetailComponent>;
        let service: BetTeamService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BetChampionTestModule],
                declarations: [BetTeamDetailComponent],
                providers: [
                    BetTeamService
                ]
            })
            .overrideTemplate(BetTeamDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BetTeamDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BetTeamService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BetTeam(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.betTeam).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
