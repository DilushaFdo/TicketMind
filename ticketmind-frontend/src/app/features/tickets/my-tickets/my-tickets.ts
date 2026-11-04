import { Component } from '@angular/core';
import { TicketService } from '../../../core/services/ticket';
import { Router } from '@angular/router';

interface Ticket {
  id: number;
  title: string;
  description: string;
  status: string;
  priority: string;
  createdAt: string;
}

@Component({
  selector: 'app-my-tickets',
  standalone: true,
  imports: [],
  templateUrl: './my-tickets.html',
  styleUrl: './my-tickets.scss'
})
export class MyTickets {

  tickets: Ticket[];

  constructor(
    private ticketService: TicketService,
    private router: Router
  ) {
    this.tickets = this.ticketService.getTickets();
  }
  

  createTicket(): void {
    this.router.navigate(['/tickets/create']);
  }

  viewTicket(ticketId: number): void {
    this.router.navigate(['/tickets', ticketId]);
}

}