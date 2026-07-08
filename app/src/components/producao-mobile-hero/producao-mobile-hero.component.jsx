import { useState } from "react";
import CalendarMonthOutlinedIcon from "@mui/icons-material/CalendarMonthOutlined";
import CategoryOutlinedIcon from "@mui/icons-material/CategoryOutlined";
import CloseIcon from "@mui/icons-material/Close";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import {
  URL_BASE_BACKDROP_TMDB,
  URL_BASE_IMAGEM_TMDB,
} from "../../constants/image-tmdb";
import { obterAnoLancamento } from "../../utils/formatar-data-lancamento";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import MobilePageHeader from "../mobile-page-header/mobile-page-header.component";
import "./producao-mobile-hero.css";

function montarUrlImagem(path, base = URL_BASE_IMAGEM_TMDB) {
  return path ? `${base}${path}` : undefined;
}

function montarPeriodo(producao) {
  if (producao.tipoMidia === "MOVIE") {
    return obterAnoLancamento(producao.dataLancamento);
  }

  const { anoPrimeiraTemporada, anoUltimaTemporada } = producao;

  if (anoPrimeiraTemporada && anoUltimaTemporada) {
    return `${anoPrimeiraTemporada} - ${anoUltimaTemporada}`;
  }

  if (anoPrimeiraTemporada) {
    return `${anoPrimeiraTemporada} - Atual`;
  }

  return null;
}

function montarTemporadas(quantidadeTemporadas) {
  if (!quantidadeTemporadas) return null;

  const sufixo = quantidadeTemporadas === 1 ? "temporada" : "temporadas";
  return `${quantidadeTemporadas} ${sufixo}`;
}

export default function ProducaoMobileHero({
  producao,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  const [overviewAberto, setOverviewAberto] = useState(false);
  const backdrop = montarUrlImagem(
    producao.backdropPath,
    URL_BASE_BACKDROP_TMDB,
  );
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

          <div className="producao-mobile-metadados">
            {periodo ? (
              <span>
                <CalendarMonthOutlinedIcon />
                {periodo}
              </span>
            ) : null}
            {temporadas ? (
              <span>
                <TvOutlinedIcon />
                {temporadas}
              </span>
            ) : null}
            {producao.genero ? (
              <span>
                <CategoryOutlinedIcon />
                {producao.genero}
              </span>
            ) : null}
          </div>

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

          {producao.providers?.length ? (
            <div className="producao-mobile-providers">
              <div>
                {producao.providers.map((provider) => (
                  <img
                    key={provider.provider_id || provider.provider_name}
                    src={montarUrlImagem(provider.logo_path)}
                    alt={provider.provider_name}
                  />
                ))}
              </div>
            </div>
          ) : null}
        </div>
      </div>

      {overviewAberto ? (
        <div
          className="producao-mobile-overview-modal"
          role="dialog"
          aria-modal="true"
          aria-label={`Sinopse de ${producao.nome}`}
          onClick={() => setOverviewAberto(false)}
        >
          <div
            className="producao-mobile-overview-modal-conteudo"
            onClick={(event) => event.stopPropagation()}
          >
            <button
              type="button"
              className="producao-mobile-overview-fechar"
              onClick={() => setOverviewAberto(false)}
              aria-label="Fechar sinopse"
            >
              <CloseIcon />
            </button>
            <strong>{producao.nome}</strong>
            <span>{producao.overview}</span>
          </div>
        </div>
      ) : null}
    </section>
  );
}
