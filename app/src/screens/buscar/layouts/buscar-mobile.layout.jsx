import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import SegmentedControl from "../../../components/segmented-control/segmented-control.component";
import ScrollFade from "../../../components/scroll-fade/scroll-fade.component";
import InputSearch from "../../../components/pesquisa/pesquisa.component";
import CardAtorBusca from "../../../components/card-ator-busca/card-ator-busca.component";
import BuscaProducaoCard from "../../../components/busca-producao-card/busca-producao-card.component";
import { URL_BASE_IMAGEM_TMDB } from "../../../constants/image-tmdb";

const CATEGORIAS = [
  { label: "Pessoas", value: "PESSOAS", Icone: PersonOutlineOutlinedIcon },
  { label: "Series", value: "SERIES", Icone: TvOutlinedIcon },
  { label: "Filmes", value: "FILMES", Icone: MovieOutlinedIcon },
];

function obterPlaceholder(categoria) {
  if (categoria === "PESSOAS") return "Buscar pessoa...";
  if (categoria === "FILMES") return "Buscar filme...";

  return "Buscar serie...";
}

function montarImagem(path) {
  return path ? `${URL_BASE_IMAGEM_TMDB}${path}` : undefined;
}

export default function BuscarMobileLayout({
  categoria,
  setCategoria,
  termoBusca,
  setTermoBusca,
  pesquisar,
  dadosBuscaInicial,
  abrirAtor,
  abrirFilme,
  abrirSerie,
}) {
  return (
    <div className="page-buscar">
      <ScrollFade />

      <main className="buscar-conteudo">
        <MobilePageHeader />

        <h1>Buscar</h1>

        <form className="buscar-form" onSubmit={pesquisar}>
          <InputSearch
            value={termoBusca}
            onChange={(event) => setTermoBusca(event.target.value)}
            placeholder={obterPlaceholder(categoria)}
            wrapperClassName="buscar-input-wrapper"
            inputClassName="buscar-input"
            mostrarIcone
            ariaLabel="Buscar"
          />
        </form>

        <SegmentedControl
          options={CATEGORIAS}
          value={categoria}
          onChange={setCategoria}
        />

        <div className="buscar-resultados">
          <section className="buscar-secao">
            <h2>
              <PersonOutlineOutlinedIcon />
              Pessoas
            </h2>
            {dadosBuscaInicial?.pessoas?.map((pessoa) => (
              <CardAtorBusca
                key={pessoa.id}
                titulo={pessoa.nome}
                imagem={montarImagem(pessoa.urlImagem)}
                formato="pessoa"
                onClick={() => abrirAtor(pessoa)}
              />
            ))}
          </section>

          <section className="buscar-secao">
            <h2>
              <MovieOutlinedIcon />
              Filmes
            </h2>
            <div className="buscar-grid-producoes">
              {dadosBuscaInicial?.filmes?.map((filme) => (
                <BuscaProducaoCard
                  key={filme.id}
                  titulo={filme.nomeProducao}
                  descricao={filme.overview}
                  imagem={montarImagem(filme.urlImagem)}
                  onClick={() => abrirFilme(filme)}
                />
              ))}
            </div>
          </section>

          <section className="buscar-secao">
            <h2>
              <TvOutlinedIcon />
              Series
            </h2>
            <div className="buscar-grid-producoes">
              {dadosBuscaInicial?.series?.map((serie) => (
                <BuscaProducaoCard
                  key={serie.id}
                  titulo={serie.nomeProducao}
                  descricao={serie.overview}
                  imagem={montarImagem(serie.urlImagem)}
                  onClick={() => abrirSerie(serie)}
                />
              ))}
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}
