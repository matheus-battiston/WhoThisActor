import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import TheatersOutlinedIcon from "@mui/icons-material/TheatersOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import MobileSegmentedControl from "../../../components/mobile-segmented-control/mobile-segmented-control.component";
import FavoritoAtorCard from "../../../components/favorito-ator-card/favorito-ator-card.component";
import FavoritoProducaoCard from "../../../components/favorito-producao-card/favorito-producao-card.component";
import TipoProducaoToggle from "../../../components/tipo-producao-toggle/tipo-producao-toggle.component";
import Loading from "../../../components/loading/loading.component";
import "./favoritos-mobile.css";

const ABAS = [
  { label: "Produções", value: "PRODUCOES", Icone: TheatersOutlinedIcon },
  { label: "Atores", value: "ATORES", Icone: PersonOutlineOutlinedIcon },
];

function obterTituloSecao(aba, tipoProducao) {
  if (aba === "ATORES") return "Atores favoritos";

  return tipoProducao === "filmes" ? "Filmes favoritos" : "Séries favoritas";
}

function obterIconeSecao(aba, tipoProducao) {
  if (aba === "ATORES") return PersonOutlineOutlinedIcon;

  return tipoProducao === "filmes" ? MovieOutlinedIcon : TvOutlinedIcon;
}

export default function FavoritosMobileLayout({
  aba,
  setAba,
  tipoProducao,
  setTipoProducao,
  atores,
  listaProducoes,
  loading,
  removendoFavorito,
  removerAtorFavorito,
  removerProducaoFavorita,
}) {
  const IconeSecao = obterIconeSecao(aba, tipoProducao);

  return (
    <div className="favoritos-mobile">
      <MobilePageHeader />

      <main className="favoritos-mobile-conteudo">
        <h1 className="favoritos-mobile-titulo">Favoritos</h1>

        <MobileSegmentedControl
          options={ABAS}
          value={aba}
          onChange={setAba}
          className="favoritos-mobile-abas"
        />

        <section className="favoritos-mobile-painel">
          <header className="favoritos-mobile-painel-header">
            <span>
              <IconeSecao />
              {obterTituloSecao(aba, tipoProducao)}
            </span>
            {aba === "PRODUCOES" ? (
              <TipoProducaoToggle
                tipoProducao={tipoProducao}
                onChange={setTipoProducao}
              />
            ) : null}
          </header>

          {aba === "ATORES" ? (
            <div className="favoritos-mobile-grid-atores">
              {atores.map((ator) => (
                <FavoritoAtorCard
                  key={ator.id}
                  ator={ator}
                  removendo={removendoFavorito}
                  onRemoverFavorito={removerAtorFavorito}
                />
              ))}
            </div>
          ) : (
            <div className="favoritos-mobile-grid-producoes">
              {listaProducoes.map((producao) => (
                <FavoritoProducaoCard
                  key={producao.id}
                  producao={producao}
                  tipoMidia={tipoProducao === "filmes" ? "MOVIE" : "TV"}
                  removendo={removendoFavorito}
                  onRemoverFavorito={removerProducaoFavorita}
                />
              ))}
            </div>
          )}
        </section>
      </main>

      {loading ? <Loading frase="Carregando favoritos" /> : null}
    </div>
  );
}
