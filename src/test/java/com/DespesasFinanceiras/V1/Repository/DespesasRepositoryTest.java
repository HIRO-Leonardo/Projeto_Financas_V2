package com.DespesasFinanceiras.V1.Repository;

import com.DespesasFinanceiras.V1.Entity.DespesasEntityTest;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnumTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesasRepositoryTest extends JpaRepository<DespesasEntityTest, Long> {

    @Query("SELECT SUM(d.valor) FROM DespesasEntity d WHERE d.situacaoEnum = :situacao")
    BigDecimal sumByTipo(@Param("situacao") SituacaoEnumTest situacao);

    @Query("SELECT d FROM DespesasEntity d WHERE EXTRACT(YEAR FROM d.localDateTime) = ?1 AND EXTRACT(MONTH FROM d.localDateTime) = ?2")
    List<DespesasEntityTest> findByAnoMesDespesa(Integer ano, Integer mes);

    @Query(value = "SELECT d.categoriaEnum AS categoria, sum(d.valor) AS valor FROM DespesasEntity d WHERE EXTRACT(MONTH FROM d.localDateTime) = :mes and EXTRACT(YEAR FROM d.localDateTime) = :ano GROUP BY d.categoriaEnum")
    List<TotalPorCategoriaTest> totalPorCategoriaPorMes(@Param("mes") Integer mes, @Param("ano") Integer ano);

    @Query("SELECT d FROM DespesasEntity d WHERE EXTRACT(YEAR FROM d.localDateTime) = ?1 AND EXTRACT(MONTH FROM d.localDateTime) = ?2")
    List<DespesasEntityTest> selectBetweenTwoMonthDespesa(Integer ano, Integer mesAnterior);

    @Query("SELECT d FROM DespesasEntity d WHERE EXTRACT(YEAR FROM d.localDateTime) = ?1 AND EXTRACT(MONTH FROM d.localDateTime) = ?2")
    List<DespesasEntityTest> selectAllDespesasPerMonth(Integer ano, Integer mes);

    List<DespesasEntityTest> findByAutor(String autor);

    Page<DespesasEntityTest> findAllByOrderByIdDesc(Pageable pageable);
}
