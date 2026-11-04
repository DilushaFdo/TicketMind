import { Component } from '@angular/core';
import { TicketService } from '../../../core/services/ticket';

import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-ticket',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './create-ticket.html',
  styleUrl: './create-ticket.scss'
})
export class CreateTicket {

  ticketForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private ticketService: TicketService
  ) {
    this.ticketForm = this.fb.group({
      title: ['', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],

      description: ['', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(1000)
      ]],

      priority: ['MEDIUM', Validators.required]
    });
  }

  onSubmit(): void {

    if (this.ticketForm.invalid) {
      this.ticketForm.markAllAsTouched();
      return;
    }

    console.log('Form submitted:', this.ticketForm.value);

    this.ticketService.createTicket(this.ticketForm.value);

    this.router.navigate(['/tickets']).then(result => {
    console.log('Navigation successful:', result);
});

  }

  cancel(): void {
    this.router.navigate(['/tickets']);
  }
}