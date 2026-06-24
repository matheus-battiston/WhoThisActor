package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritaSerieRepository extends JpaRepository<FavoritaSerie, Long> {
    boolean existsByUsuarioIdAndSerieId(Long usuarioId, Long serieId);

    Optional<FavoritaSerie> findByUsuarioIdAndSerieId(Long usuarioId, Long producaoId);

    @Query("""
    SELECT fp.serie.id
    FROM FavoritaSerie fp
    WHERE fp.usuario.authUserId = :authUserId
""")
    List<Long> findSerieIdByAuthUserId(Long authUserId);


    @Query("""
    select count(fp) > 0
    from FavoritaSerie fp
    where fp.usuario.authUserId = :authUserId
      and fp.serie.id = :producaoId
""")
    boolean existsByAuthUserIdAndProducaoId(
            @Param("authUserId") Long authUserId,
            @Param("producaoId") Long producaoId
    );


    @Query("""
SELECT fp.serie
FROM FavoritaSerie fp
WHERE fp.usuario.authUserId = :authUserId
""")
    List<Serie> findSeriesByAuthUserId(Long authUserId);

}
