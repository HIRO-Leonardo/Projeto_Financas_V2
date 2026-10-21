package com.DespesasFinanceiras.V1.Repository;



import com.DespesasFinanceiras.V1.Entity.ReceitaEntityTest;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnumTest;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnumTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ReceitaRepositoryTest extends JpaRepository<ReceitaEntityTest,Long>, JpaSpecificationExecutor<ReceitaEntityTest> {

    static Specification<ReceitaEntityTest> comFiltros(
                CategoriaReceitaEnumTest categoriaReceitaEnumTest,
                SituacaoEnumTest situacaoEnumTest,
                Integer mes) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (categoriaReceitaEnumTest != null) {
                    predicates.add((Predicate) criteriaBuilder.equal(root.get("categoriaEnum"), categoriaReceitaEnumTest));
                }
                if (situacaoEnumTest != null) {
                    predicates.add((Predicate) criteriaBuilder.equal(root.get("situacaoEnum"), situacaoEnumTest));
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
    BigDecimal sumByType(@Param("categoria") CategoriaReceitaEnumTest categoria);

    @Query("SELECT SUM(r.valor) FROM ReceitaEntity r WHERE r.situacaoEnum = :situacao")
    BigDecimal sumBySituacion(@Param("situacao") SituacaoEnumTest situacao);

    @Query("SELECT r FROM ReceitaEntity r WHERE EXTRACT(YEAR FROM r.localDateTimeEntrada) = ?1 AND EXTRACT(MONTH FROM r.localDateTimeEntrada) = ?2")
    List<ReceitaEntityTest> findByAnoMes(Integer ano, Integer mes);

    @Query("SELECT r FROM ReceitaEntity r WHERE EXTRACT(YEAR FROM r.localDateTimeEntrada) = :ano AND EXTRACT(MONTH FROM r.localDateTimeEntrada) = :mes")
    List<ReceitaEntityTest> selectBetweenTwoMonth(@Param("ano") Integer ano, @Param("mes") Integer mesAnterior);

    @Query("SELECT r FROM ReceitaEntity r WHERE EXTRACT(YEAR FROM r.localDateTimeEntrada) = :ano AND EXTRACT(MONTH FROM r.localDateTimeEntrada) = :mes")
    List<ReceitaEntityTest> listAllReceitaPerMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    List<ReceitaEntityTest> findByAutor(String autor);

    Page<ReceitaEntityTest> findAllByOrderByIdDesc(Pageable pageable);

}
