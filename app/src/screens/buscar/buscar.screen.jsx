import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import MobilePageHeader from "../../components/mobile-page-header/mobile-page-header.component";
import MobileSegmentedControl from "../../components/mobile-segmented-control/mobile-segmented-control.component";
import InputSearch from "../../components/pesquisa/pesquisa.component";
import CardAtorBusca from "../../components/card-ator-busca/card-ator-busca.component";
import BuscaProducaoCard from "../../components/busca-producao-card/busca-producao-card.component";
import Loading from "../../components/loading/loading.component";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import {
  pesquisarAtorPorNome,
  pesquisarFilmePorNome,
  pesquisarSeriePorNome,
  useBuscarInformacoesIniciaisBusca,
} from "../../api/generated/api";
import "./buscar.css";

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

export function BuscarScreen() {
  const navigate = useNavigate();
  const [categoria, setCategoria] = useState("PESSOAS");
  const [termoBusca, setTermoBusca] = useState("");
  const [loadingManual, setLoadingManual] = useState(false);
  const buscaInicial = useBuscarInformacoesIniciaisBusca();
  const dadosBuscaInicial = buscaInicial.data?.data;

  const navegarComResultadoAtor = (dadosPesquisaAtor) => {
    const { contratoResposta, atores } = dadosPesquisaAtor;

    if (contratoResposta === "UNIQUE_MATCH") {
      setLoadingManual(true);
      navigate(`/ator/${atores[0].id}`);
      return true;
    }

    if (contratoResposta === "MULTIPLE_MATCH") {
      setLoadingManual(true);
      navigate("/opcoesAtores", {
        state: { opcoes: atores },
      });
      return true;
    }

    return false;
  };

  const navegarComResultadoProducao = (dadosPesquisa, tipoMidia) => {
    if (dadosPesquisa.length === 1) {
      setLoadingManual(true);
      navigate(`/exibirElenco/${tipoMidia}/${dadosPesquisa[0].id}`);
      return true;
    }

    if (dadosPesquisa.length > 1) {
      setLoadingManual(true);
      navigate("/opcoesProducoes", {
        state: { opcoes: dadosPesquisa, tipoMidia },
      });
      return true;
    }

    return false;
  };

  const pesquisaAtor = useMutation({
    mutationFn: () => pesquisarAtorPorNome({ nome: termoBusca }),
    onSuccess: (response) => {
      if (!navegarComResultadoAtor(response.data)) {
        setLoadingManual(false);
      }
    },
    onError: () => setLoadingManual(false),
  });

  const pesquisaSerie = useMutation({
    mutationFn: () => pesquisarSeriePorNome({ nome: termoBusca }),
    onSuccess: (response) => {
      if (!navegarComResultadoProducao(response.data, "TV")) {
        setLoadingManual(false);
      }
    },
    onError: () => setLoadingManual(false),
  });

  const pesquisaFilme = useMutation({
    mutationFn: () => pesquisarFilmePorNome({ nome: termoBusca }),
    onSuccess: (response) => {
      if (!navegarComResultadoProducao(response.data, "MOVIE")) {
        setLoadingManual(false);
      }
    },
    onError: () => setLoadingManual(false),
  });

  const pesquisando =
    pesquisaAtor.isPending ||
    pesquisaSerie.isPending ||
    pesquisaFilme.isPending;
  const exibirLoading = loadingManual || pesquisando || buscaInicial.isLoading;
  const fraseLoading = buscaInicial.isLoading
    ? "Carregando busca..."
    : "Pesquisando...";

  const pesquisar = (event) => {
    event.preventDefault();
    if (!termoBusca.trim()) return;

    if (categoria === "PESSOAS") {
      pesquisaAtor.mutate();
      return;
    }

    if (categoria === "FILMES") {
      pesquisaFilme.mutate();
      return;
    }

    pesquisaSerie.mutate();
  };

  return (
    <div className="page-buscar">
      <MobilePageHeader />

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

        <MobileSegmentedControl
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
                onClick={() => navigate(`/ator/${pessoa.id}`)}
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
                  onClick={() => navigate(`/exibirElenco/MOVIE/${filme.id}`)}
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
                  onClick={() => navigate(`/exibirElenco/TV/${serie.id}`)}
                />
              ))}
            </div>
          </section>
        </div>
      </main>

      {exibirLoading ? <Loading frase={fraseLoading} /> : null}
    </div>
  );
}
