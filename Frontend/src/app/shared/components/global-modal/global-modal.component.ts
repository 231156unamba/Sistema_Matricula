import { Component, OnInit, OnDestroy, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalService, ModalOptions } from '../../services/modal.service';
import { Subscription } from 'rxjs';
import { ModalComponent } from '../modal/modal.component';

@Component({
  selector: 'app-global-modal',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  template: `
    <app-modal
      [isOpen]="!!modalOptions"
      [title]="modalOptions?.title || ''"
      [type]="modalOptions?.type || 'info'"
      [showFooter]="true"
      [showCancelButton]="modalOptions?.showCancel ?? true"
      [showConfirmButton]="true"
      [confirmText]="modalOptions?.confirmText || 'Aceptar'"
      [cancelText]="modalOptions?.cancelText || 'Cancelar'"
      (onClose)="closeModal()"
      (onConfirm)="confirmModal()"
    >
      <div class="modal-message" *ngIf="modalOptions?.message">
        {{ modalOptions?.message || '' }}
      </div>
    </app-modal>
  `,
  styles: [`
    .modal-message {
      text-align: center;
      font-size: 1.1rem;
      line-height: 1.6;
      color: #374151;
      margin: 1rem 0;
    }
  `]
})
export class GlobalModalComponent implements OnInit, OnDestroy {
  modalOptions: ModalOptions | null = null;
  private modalSubscription: Subscription | undefined;

  constructor(
    private modalService: ModalService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    this.modalSubscription = this.modalService.modal$.subscribe(
      (options: ModalOptions | null) => {
        // Asegura actualización inmediata incluso si el request se resolvió fuera de Angular
        this.zone.run(() => {
          this.modalOptions = options;
          this.cdr.detectChanges();
        });
      }
    );
  }

  ngOnDestroy(): void {
    if (this.modalSubscription) {
      this.modalSubscription.unsubscribe();
    }
  }

  closeModal(): void {
    if (this.modalOptions?.type === 'confirmation') {
      this.modalService.confirm(false);
    } else {
      this.modalService.hide();
    }
  }

  confirmModal(): void {
    if (this.modalOptions?.type === 'confirmation') {
      this.modalService.confirm(true);
    } else {
      this.modalService.hide();
    }
  }
}
