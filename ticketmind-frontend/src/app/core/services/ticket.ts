import { Injectable } from '@angular/core';

export interface Ticket {
  id: number;
  title: string;
  description: string;
  status: string;
  priority: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private tickets: Ticket[] = [
    {
      id: 1001,
      title: 'Unable to login to my account',
      description: 'I cannot access my account using my credentials.',
      status: 'OPEN',
      priority: 'HIGH',
      createdAt: '2026-11-01'
    },
    {
      id: 1002,
      title: 'Payment transaction failed',
      description: 'My payment was unsuccessful but money was deducted.',
      status: 'IN_PROGRESS',
      priority: 'URGENT',
      createdAt: '2026-11-02'
    }
  ];

  getTickets(): Ticket[] {
    return this.tickets;
  }

  createTicket(ticketData: {
    title: string;
    description: string;
    priority: string;
  }): void {
    const newTicket: Ticket = {
      id: Date.now(),
      ...ticketData,
      status: 'OPEN',
      createdAt: new Date().toISOString().split('T')[0]
    };

    this.tickets.unshift(newTicket);
  }

  updateTicketStatus(ticketId: number, newStatus: string): void {
  const ticket = this.tickets.find(
    ticket => ticket.id === ticketId
  );

  if (ticket) {
    ticket.status = newStatus;
  }
}
}