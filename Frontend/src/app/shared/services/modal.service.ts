import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface ModalOptions {
  title: string;
  message?: string;
  type: 'success' | 'error' | 'warning' | 'info' | 'confirmation';
  showCancel?: boolean;
  confirmText?: string;
  cancelText?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private modalSubject = new Subject<ModalOptions | null>();
  public modal$ = this.modalSubject.asObservable();
  private confirmationSubject = new Subject<boolean>();

  show(options: ModalOptions): void {
    this.modalSubject.next(options);
  }

  showSuccess(title: string, message?: string): void {
    this.show({
      title,
      message,
      type: 'success',
      showCancel: false,
      confirmText: 'Aceptar'
    });
  }

  showError(title: string, message?: string): void {
    this.show({
      title,
      message,
      type: 'error',
      showCancel: false,
      confirmText: 'Aceptar'
    });
  }

  showWarning(title: string, message?: string): void {
    this.show({
      title,
      message,
      type: 'warning',
      showCancel: false,
      confirmText: 'Aceptar'
    });
  }

  showInfo(title: string, message?: string): void {
    this.show({
      title,
      message,
      type: 'info',
      showCancel: false,
      confirmText: 'Aceptar'
    });
  }

  showConfirmation(
    title: string, 
    message?: string, 
    confirmText: string = 'Confirmar',
    cancelText: string = 'Cancelar'
  ): Observable<boolean> {
    this.show({
      title,
      message,
      type: 'confirmation',
      showCancel: true,
      confirmText,
      cancelText
    });
    return this.confirmationSubject.asObservable();
  }

  confirm(result: boolean): void {
    this.confirmationSubject.next(result);
    this.hide();
  }

  hide(): void {
    this.modalSubject.next(null);
  }
}
