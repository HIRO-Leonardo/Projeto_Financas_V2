package com.DespesasFinanceiras.V1.Repository;



import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<ReceitaEntity,Long>, JpaSpecificationExecutor<ReceitaEntity> {

    static Specification<ReceitaEntity> comFiltros(
                CategoriaReceitaEnum categoriaReceitaEnum,
                SituacaoEnum situacaoEnum,
                Integer mes) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (categoriaReceitaEnum != null) {
                    predicates.add((Predicate) criteriaBuilder.equal(root.get("categoriaEnum"), categoriaReceitaEnum));
                }
                if (situacaoEnum != null) {
                    predicates.add((Predicate) criteriaBuilder.equal(root.get("situacaoEnum"), situacaoEnum));
                }
                if (mes != null) {
                    predicates.add((Predicate) criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("localDateTimeEntrada")), mes));
                }
                return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
            };
    }

    @Query("SELECT SUM(r.valor) FROM ReceitaEntity r")
    BigDecimal sumAllByValor();

    @Query("SELECT SUM(r.valor) FROM ReceitaEntity r WHERE r.categoriaReceitaEnum = :categoria")
    BigDecimal sumByType(@Param("categoria") CategoriaReceitaEnum categoria);

    @Query("SELECT SUM(r.valor) FROM ReceitaEntity r WHERE r.situacaoEnum = :situacao")
    BigDecimal sumBySituacion(@Param("situacao") SituacaoEnum situacao);

    @Query("SELECT r FROM ReceitaEntity r WHERE EXTRACT(YEAR FROM r.localDateTimeEntrada) = ?1 AND EXTRACT(MONTH FROM r.localDateTimeEntrada) = ?2")
    List<ReceitaEntity> findByAnoMes(Integer ano, Integer mes);

    @Query("SELECT r FROM ReceitaEntity r WHERE EXTRACT(YEAR FROM r.localDateTimeEntrada) = :ano AND EXTRACT(MONTH FROM r.localDateTimeEntrada) = :mes")
    List<ReceitaEntity> selectBetweenTwoMonth(@Param("ano") Integer ano, @Param("mes") Integer mesAnterior);

    @Query("SELECT r FROM ReceitaEntity r WHERE EXTRACT(YEAR FROM r.localDateTimeEntrada) = :ano AND EXTRACT(MONTH FROM r.localDateTimeEntrada) = :mes")
    List<ReceitaEntity> listAllReceitaPerMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    List<ReceitaEntity> findByAutor(String autor);

    Page<ReceitaEntity> findAllByOrderByIdDesc(Pageable pageable);

}
