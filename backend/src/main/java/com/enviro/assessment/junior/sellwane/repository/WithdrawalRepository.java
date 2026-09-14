package com.enviro.assessment.junior.sellwane.repository;

import com.enviro.assessment.junior.sellwane.model.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<WithdrawalNotice, Long> {

    /**
     * Spring Data JPA reads this method NAME and generates the SQL for us.
     * "findByProduct_Investor_Id" walks the object graph:
     *   WithdrawalNotice -> product -> investor -> id
     * and becomes (roughly):
     *   SELECT * FROM withdrawal_notice w
     *   JOIN product p ON w.product_id = p.id
     *   WHERE p.investor_id = ?
     * ordered by most recent first - handy for the "Withdrawal history table".
     */
    List<WithdrawalNotice> findByProduct_Investor_IdOrderByRequestDateDesc(Long investorId);
}
