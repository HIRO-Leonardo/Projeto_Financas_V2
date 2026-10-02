import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaGeralComponent } from './lista-geral.component';

describe('ListaGeralComponent', () => {
  let component: ListaGeralComponent;
  let fixture: ComponentFixture<ListaGeralComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaGeralComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaGeralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
