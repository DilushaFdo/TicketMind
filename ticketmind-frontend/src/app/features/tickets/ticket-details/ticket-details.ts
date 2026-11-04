import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Ticket, TicketService } from '../../../core/services/ticket';

@Component({
  selector: 'app-ticket-details',
  standalone: true,
  imports: [],
  templateUrl: './ticket-details.html',
  styleUrl: './ticket-details.scss'
})
export class TicketDetails {

  ticket?: Ticket;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService,
    private router: Router
  ) {
    const ticketId = Number(this.route.snapshot.paramMap.get('id'));

    this.ticket = this.ticketService
      .getTickets()
      .find(ticket => ticket.id === ticketId);
  }

  goBack(): void {
    this.router.navigate(['/tickets']);
  }

  updateStatus(event: Event): void {
  const newStatus = (event.target as HTMLSelectElement).value;

  if (!this.ticket) {
    return;
  }

  this.ticketService.updateTicketStatus(
    this.ticket.id,
    newStatus
  );

  this.ticket.status = newStatus;
}
}