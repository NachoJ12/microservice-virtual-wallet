package com.dh.wallet.repository;

import com.dh.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet,Long> {

    @Query("SELECT w FROM Wallet w INNER JOIN Currency c ON c.id = w.currency WHERE w.documentType = ?1 AND w.document = ?2 AND c.code = ?3")
    Optional<Wallet> findByDocumentAndCode(String documentType, String document, String code);

    @Query("SELECT w FROM Wallet w WHERE w.documentType = ?1 AND w.document = ?2")
    List<Wallet> findByDocument(String documentType, String document);
}
