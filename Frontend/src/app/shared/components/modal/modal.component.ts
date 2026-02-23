import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal-overlay" *ngIf="isOpen" (click)="onOverlayClick($event)">
      <div class="modal-content" (click)="$event.stopPropagation()">
        <div class="modal-header">
          <h2>{{ title }}</h2>
          <button class="modal-close" (click)="close()">&times;</button>
        </div>
        <div class="modal-body">
          <ng-content></ng-content>
        </div>
        <div class="modal-footer" *ngIf="showFooter">
          <button class="btn btn-secondary" (click)="close()" *ngIf="showCancelButton">
            {{ cancelText }}
          </button>
          <button class="btn btn-primary" (click)="confirm()" *ngIf="showConfirmButton">
            {{ confirmText }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class ModalComponent {
  @Input() isOpen = false;
  @Input() title = 'Modal';
  @Input() showFooter = true;
  @Input() showCancelButton = true;
  @Input() showConfirmButton = true;
  @Input() cancelText = 'Cancelar';
  @Input() confirmText = 'Confirmar';
  
  @Output() onClose = new EventEmitter<void>();
  @Output() onConfirm = new EventEmitter<void>();

  close() {
    this.isOpen = false;
    this.onClose.emit();
  }

  confirm() {
    this.onConfirm.emit();
  }

  onOverlayClick(event: MouseEvent) {
    this.close();
  }
}
