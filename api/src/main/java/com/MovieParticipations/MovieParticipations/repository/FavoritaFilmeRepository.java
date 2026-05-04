package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.FavoritaFilme;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritaFilmeRepository extends JpaRepository<FavoritaFilme, Long> {
    boolean existsByUsuarioIdAndFilmeId(Long usuarioId, Long filmeId);

    Optional<FavoritaFilme> findByUsuarioIdAndFilmeId(Long usuarioId, Long filmeId);

    @Query("""
            SELECT ff.filme
            FROM FavoritaFilme ff
            WHERE ff.usuario.authUserId = :authUserId
            """)
    List<Filme> findFilmesByAuthUserId(Long authUserId);

    @Query("""
            select count(ff) > 0
            from FavoritaFilme ff
            where ff.usuario.authUserId = :authUserId
              and ff.filme.id = :filmeId
            """)
    boolean existsByAuthUserIdAndFilmeId(
            @Param("authUserId") Long authUserId,
            @Param("filmeId") Long filmeId
    );

    @Query("""
    SELECT ff.filme.id
    FROM FavoritaFilme ff
    WHERE ff.usuario.authUserId = :authUserId
""")
    List<Long> findFilmesIdByAuthUserId(Long authUserId);}
