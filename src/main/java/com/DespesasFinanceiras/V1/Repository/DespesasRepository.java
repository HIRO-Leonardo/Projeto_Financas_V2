package com.DespesasFinanceiras.V1.Repository;

import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesasRepository extends JpaRepository<DespesasEntity, Long> {

    @Query("SELECT SUM(d.valor) FROM DespesasEntity d WHERE d.situacaoEnum = :situacao")
    BigDecimal sumByTipo(@Param("situacao") SituacaoEnum situacao);

    @Query("SELECT d FROM DespesasEntity d WHERE EXTRACT(YEAR FROM d.localDateTime) = ?1 AND EXTRACT(MONTH FROM d.localDateTime) = ?2")
    List<DespesasEntity> findByAnoMesDespesa(Integer ano, Integer mes);

    @Query(value = "SELECT d.categoriaEnum AS categoria, sum(d.valor) AS valor FROM DespesasEntity d WHERE EXTRACT(MONTH FROM d.localDateTime) = :mes and EXTRACT(YEAR FROM d.localDateTime) = :ano GROUP BY d.categoriaEnum")
    List<TotalPorCategoria> totalPorCategoriaPorMes(@Param("mes") Integer mes, @Param("ano") Integer ano);

    @Query("SELECT d FROM DespesasEntity d WHERE EXTRACT(YEAR FROM d.localDateTime) = ?1 AND EXTRACT(MONTH FROM d.localDateTime) = ?2")
    List<DespesasEntity> selectBetweenTwoMonthDespesa(Integer ano, Integer mesAnterior);

    @Query("SELECT d FROM DespesasEntity d WHERE EXTRACT(YEAR FROM d.localDateTime) = ?1 AND EXTRACT(MONTH FROM d.localDateTime) = ?2")
    List<DespesasEntity> selectAllDespesasPerMonth(Integer ano, Integer mes);

    List<DespesasEntity> findByAutor(String autor);

    Page<DespesasEntity> findAllByOrderByIdDesc(Pageable pageable);
}
