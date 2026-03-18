import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal-overlay" *ngIf="isOpen" (click)="onOverlayClick($event)">
      <div class="modal-content" [ngClass]="['modal-' + type, showImageSide ? '' : 'no-image-side']" (click)="$event.stopPropagation()">
        
        <div class="modal-main-row">
          <div class="modal-info-side">
            <button class="modal-close" (click)="close()"><i class="bi bi-x"></i></button>
            <h2 class="modal-title">{{ title }}</h2>
            <div class="modal-body">
              <ng-content></ng-content>
            </div>
            <div class="modal-footer" *ngIf="showFooter">
              <button class="btn btn-secondary-premium" (click)="close()" *ngIf="showCancelButton && type === 'confirmation'">
                {{ cancelText }}
              </button>
              <button class="btn btn-premium-modal" [ngClass]="getButtonClass()" (click)="confirm()" *ngIf="showConfirmButton">
                {{ confirmText }}
              </button>
            </div>
          </div>
          
          <div class="modal-image-side" *ngIf="showImageSide">
            <img [src]="getImagePath()" alt="status-icon" class="status-img">
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrl: './modal.component.css'
})
export class ModalComponent {
  @Input() isOpen = false;
  @Input() title = 'Modal';
  @Input() type: 'success' | 'error' | 'warning' | 'info' | 'confirmation' = 'info';
  @Input() showFooter = true;
  @Input() showCancelButton = true;
  @Input() showConfirmButton = true;
  @Input() showImageSide = true;
  @Input() cancelText = 'Cancelar';
  @Input() confirmText = 'Confirmar';

  @Output() onClose = new EventEmitter<void>();
  @Output() onConfirm = new EventEmitter<void>();

  getImagePath(): string {
    switch (this.type) {
      case 'success': return '/succes.png';
      case 'error':
      case 'warning': return '/error.png';
      case 'confirmation':
      case 'info':
      default: return '/valid.png';
    }
  }

  getButtonClass(): string {
    switch (this.type) {
      case 'success':
        return 'btn-success';
      case 'error':
        return 'btn-danger';
      case 'warning':
        return 'btn-warning';
      case 'confirmation':
        return 'btn-primary';
      case 'info':
      default:
        return 'btn-info';
    }
  }

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
