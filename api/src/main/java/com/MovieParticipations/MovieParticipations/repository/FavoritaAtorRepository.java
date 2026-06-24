package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritaAtorRepository extends JpaRepository<FavoritaAtor, Long> {
    boolean existsByUsuarioIdAndAtorId(Long usuarioId, Long atorId);

    Optional<FavoritaAtor> findByUsuarioIdAndAtorId(Long usuarioId, Long idAtor);

    @Query("""
    select count(fa) > 0
    from FavoritaAtor fa
    where fa.usuario.authUserId = :authUserId
      and fa.ator.id = :producaoId
""")
    boolean existsByAuthUserIdAndAtorId(Long authUserId, Long producaoId);

    @Query("""
    SELECT fa.ator
    FROM FavoritaAtor fa
    WHERE fa.usuario.authUserId = :authUserId
    """)
    List<Ator> findAtoresByAuthUserId(Long authUserId);
}
