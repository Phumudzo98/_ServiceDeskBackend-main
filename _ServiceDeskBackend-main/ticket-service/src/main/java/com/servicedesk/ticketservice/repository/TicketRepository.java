package com.servicedesk.ticketservice.repository;

import com.servicedesk.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query(value = "SELECT * FROM ticket WHERE requested_by=:customerUserId ORDER BY created_at DESC",nativeQuery = true)
    List<Ticket> findByByCustomerUserId(UUID customerUserId);

    @Query(value = "SELECT * FROM ticket WHERE assigned_to=:customerAgentId ORDER BY created_at DESC",nativeQuery = true)
    List<Ticket> findByCustomerAgentId(UUID customerAgentId);

    @Query(value = "SELECT * FROM ticket WHERE status=:status AND company_id=:companyId",nativeQuery = true)
    List<Ticket> findResolvedTickets(String status,UUID companyId);
    @Query(value = "SELECT SUM(CASE WHEN status = :status THEN 1 ELSE 0 END) AS totalTicketValue " +
            "FROM ticket " +
            "WHERE company_id = UNHEX(REPLACE(:companyId, '-', '')) " +
            "AND MONTH(updated_at) = MONTH(:updatedAt) " +
            "AND YEAR(updated_at) = YEAR(:updatedAt)",
            nativeQuery = true)
    int findTicketsEachMonth (@Param("status") String status, @Param("companyId") String companyId, @Param("updatedAt") Date updatedAt);

    List<Ticket> findByCompanyId(UUID companyId);

    @Query(value = "SELECT SUM(CASE WHEN status = :status THEN 1 ELSE 0 END) AS totalTicketValue " +
            "FROM ticket " +
            "WHERE company_id = UNHEX(REPLACE(:companyId, '-', '')) " +
            "AND MONTH(created_at) = MONTH(:createdAt) " +
            "AND YEAR(created_at) = YEAR(:createdAt)",
            nativeQuery = true)
    int findTicketsOPenEachMonth (@Param("status") String status, @Param("companyId") String companyId, @Param("createdAt") Date updatedAt);

}
