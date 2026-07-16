import { useState } from "react";
import {
  montarPeriodo,
  montarTemporadas,
  montarUrlBackdrop,
  montarUrlImagem,
} from "../producao-hero/producao-hero.utils";
import ProducaoHeroMetadata from "../producao-hero/producao-hero-metadata.component";
import ProducaoHeroProviders from "../producao-hero/producao-hero-providers.component";
import ProducaoOverviewModal from "../producao-overview-modal/producao-overview-modal.component";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import MobilePageHeader from "../mobile-page-header/mobile-page-header.component";
import "./producao-mobile-hero.css";

export default function ProducaoMobileHero({
  producao,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  const [overviewAberto, setOverviewAberto] = useState(false);
  const backdrop = montarUrlBackdrop(producao.backdropPath);
  const poster = montarUrlImagem(producao.imagem);
  const periodo = montarPeriodo(producao);
  const temporadas = montarTemporadas(producao.quantidadeTemporadas);

  return (
    <section className="producao-mobile-hero">
      <div
        className="producao-mobile-backdrop"
        style={backdrop ? { backgroundImage: `url(${backdrop})` } : undefined}
      />

      <MobilePageHeader />

      <div className="producao-mobile-info">
        <div className="producao-mobile-poster-wrapper">
          <LoadingImage
            src={poster}
            alt={producao.nome}
            className="producao-mobile-poster"
          />
          {isAuthenticated ? (
            <div className="producao-mobile-favorito">
              <FavoritoIcon
                ativo={favoritado}
                onClick={favoritar}
                disabled={favoritoPendente}
              />
            </div>
          ) : null}
        </div>

        <div className="producao-mobile-texto">
          <h1>{producao.nome}</h1>

          <ProducaoHeroMetadata
            periodo={periodo}
            temporadas={temporadas}
            genero={producao.genero}
            className="producao-mobile-metadados"
          />

          {producao.overview ? (
            <button
              type="button"
              className="producao-mobile-overview"
              onClick={() => setOverviewAberto(true)}
              aria-expanded={overviewAberto}
            >
              {producao.overview}
            </button>
          ) : null}

          <ProducaoHeroProviders
            providers={producao.providers}
            className="producao-mobile-providers"
          />
        </div>
      </div>

      {overviewAberto ? (
        <ProducaoOverviewModal
          nome={producao.nome}
          overview={producao.overview}
          onClose={() => setOverviewAberto(false)}
        />
      ) : null}
    </section>
  );
}
