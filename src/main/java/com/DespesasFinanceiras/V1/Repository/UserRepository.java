package com.DespesasFinanceiras.V1.Repository;

import com.DespesasFinanceiras.V1.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserDetails> findUserByEmail(String username);
}
