import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import SegmentedControl from "../../../components/segmented-control/segmented-control.component";
import InputSearch from "../../../components/pesquisa/pesquisa.component";
import BuscaProducaoWebCard from "../../../components/busca-producao-web-card/busca-producao-web-card.component";
import PessoaWebCard from "../../../components/pessoa-web-card/pessoa-web-card.component";

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
  return path || undefined;
}

export default function BuscarWebLayout({
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
    <div className="page-buscar page-buscar-web">
      <main className="buscar-conteudo">
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
            <div className="buscar-grid-pessoas">
              {dadosBuscaInicial?.pessoas?.map((pessoa) => (
                <PessoaWebCard
                  key={pessoa.id}
                  nome={pessoa.nome}
                  imagem={montarImagem(pessoa.urlImagem)}
                  onClick={() => abrirAtor(pessoa)}
                />
              ))}
            </div>
          </section>

          <section className="buscar-secao">
            <h2>
              <MovieOutlinedIcon />
              Filmes
            </h2>
            <div className="buscar-grid-producoes">
              {dadosBuscaInicial?.filmes?.map((filme) => (
                <BuscaProducaoWebCard
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
                <BuscaProducaoWebCard
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
