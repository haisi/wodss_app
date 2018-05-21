/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BetChampionTestModule } from '../../../test.module';
import { BetDetailComponent } from '../../../../../../main/webapp/app/entities/bet/bet-detail.component';
import { BetService } from '../../../../../../main/webapp/app/entities/bet/bet.service';
import { Bet } from '../../../../../../main/webapp/app/entities/bet/bet.model';

describe('Component Tests', () => {

    describe('Bet Management Detail Component', () => {
        let comp: BetDetailComponent;
        let fixture: ComponentFixture<BetDetailComponent>;
        let service: BetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BetChampionTestModule],
                declarations: [BetDetailComponent],
                providers: [
                    BetService
                ]
            })
            .overrideTemplate(BetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Bet(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.bet).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
