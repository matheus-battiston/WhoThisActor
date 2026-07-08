import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import Loading from "../../components/loading/loading.component";
import {
  pesquisarAtorPorNome,
  pesquisarFilmePorNome,
  pesquisarSeriePorNome,
  useBuscarInformacoesIniciaisBusca,
} from "../../api/generated/api";
import BuscarMobileLayout from "./layouts/buscar-mobile.layout";
import BuscarWebLayout from "./layouts/buscar-web.layout";
import "./buscar.css";

export function BuscarScreen() {
  const navigate = useNavigate();
  const isMobile = useIsMobile();
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

  const layoutProps = {
    categoria,
    setCategoria,
    termoBusca,
    setTermoBusca,
    pesquisar,
    dadosBuscaInicial,
    abrirAtor: (pessoa) => navigate(`/ator/${pessoa.id}`),
    abrirFilme: (filme) => navigate(`/exibirElenco/MOVIE/${filme.id}`),
    abrirSerie: (serie) => navigate(`/exibirElenco/TV/${serie.id}`),
  };

  return (
    <>
      {isMobile ? (
        <BuscarMobileLayout {...layoutProps} />
      ) : (
        <BuscarWebLayout {...layoutProps} />
      )}

      {exibirLoading ? <Loading frase={fraseLoading} /> : null}
    </>
  );
}
