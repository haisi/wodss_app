/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BetChampionTestModule } from '../../../test.module';
import { BetComponent } from '../../../../../../main/webapp/app/entities/bet/bet.component';
import { BetService } from '../../../../../../main/webapp/app/entities/bet/bet.service';
import { Bet } from '../../../../../../main/webapp/app/entities/bet/bet.model';

describe('Component Tests', () => {

    describe('Bet Management Component', () => {
        let comp: BetComponent;
        let fixture: ComponentFixture<BetComponent>;
        let service: BetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BetChampionTestModule],
                declarations: [BetComponent],
                providers: [
                    BetService
                ]
            })
            .overrideTemplate(BetComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BetComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BetService);
        });
    });

});
