import {
  montarPeriodo,
  montarTemporadas,
  montarUrlBackdrop,
  montarUrlImagem,
} from "../producao-hero/producao-hero.utils";
import ProducaoHeroMetadata from "../producao-hero/producao-hero-metadata.component";
import ProducaoHeroProviders from "../producao-hero/producao-hero-providers.component";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./producao-web-hero.css";

export default function ProducaoWebHero({
  producao,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  const backdrop = montarUrlBackdrop(producao.backdropPath);
  const poster = montarUrlImagem(producao.imagem);
  const periodo = montarPeriodo(producao);
  const temporadas = montarTemporadas(producao.quantidadeTemporadas);

  return (
    <section className="producao-web-hero">
      <div
        className="producao-web-backdrop"
        style={backdrop ? { backgroundImage: `url(${backdrop})` } : undefined}
      />

      <div className="producao-web-info">
        <div className="producao-web-poster-wrapper">
          <LoadingImage
            src={poster}
            alt={producao.nome}
            className="producao-web-poster"
          />
          {isAuthenticated ? (
            <div className="producao-web-favorito">
              <FavoritoIcon
                ativo={favoritado}
                onClick={favoritar}
                disabled={favoritoPendente}
              />
            </div>
          ) : null}
        </div>

        <div className="producao-web-texto">
          <h1>{producao.nome}</h1>

          <ProducaoHeroMetadata
            periodo={periodo}
            temporadas={temporadas}
            genero={producao.genero}
            className="producao-web-metadados"
          />

          {producao.overview ? (
            <p className="producao-web-overview">{producao.overview}</p>
          ) : null}

          <ProducaoHeroProviders
            providers={producao.providers}
            className="producao-web-providers"
          />
        </div>
      </div>

    </section>
  );
}
