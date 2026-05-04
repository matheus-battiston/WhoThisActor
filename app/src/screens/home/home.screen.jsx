import React, { useState } from "react";
import Logo from "../../components/logo/logo.component";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import "./home-screen.css";
import { useNomeAtor } from "../../hooks/use-nome-ator/use-nome-ator.hook";
import { useNomeSerie } from "../../hooks/use-nome-serie/use-nome-serie.hook";
import PesquisaAtor from "../../components/pesquisa-ator/pesquisa-ator.component";
import Tab from "../../components/tab/tab.component";
import PesquisaElenco from "../../components/pesquisa-elenco/pesquisa-elenco.component";
import TirarFoto from "../../components/tirar-foto/tirar-foto.component";
import MenuUser from "../../components/menu/menu-user.component";
import HomeLoading from "../../components/home-loading/home-loading.component";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import Loading from "../../components/loading/loading.component";
import {
  pesquisarAtorPorNome,
  pesquisarFilmePorNome,
  pesquisarSeriePorNome,
} from "../../api/generated/api";

export function HomeScreen() {
  const navigate = useNavigate();

  const { mudanca, atorNome } = useNomeAtor();
  const { changeSerie, serieNome } = useNomeSerie();

  const [loadingManual, setLoadingManual] = useState(false);
  const [tab, setTab] = useState("FOTO");
  const [tipo, setTipo] = useState("TV");
  const [fraseLoading, setFraseLoading] = useState("Carregando...");

  const { isAuthenticated, authChecked } = useAuth();

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

  const navegarComResultadoElenco = (dadosPesquisaElenco) => {
    if (dadosPesquisaElenco.length === 1) {
      setLoadingManual(true);
      navigate(`/exibirElenco/${tipo}/${dadosPesquisaElenco[0].id}`);
      return true;
    }

    if (dadosPesquisaElenco.length > 1) {
      setLoadingManual(true);
      navigate("/opcoesProducoes", {
        state: { opcoes: dadosPesquisaElenco, tipoMidia: tipo },
      });
      return true;
    }

    return false;
  };

  const pesquisaAtor = useMutation({
    mutationFn: () => pesquisarAtorPorNome({ nome: atorNome }),
    onSuccess: (response) => {
      if (!navegarComResultadoAtor(response.data)) {
        setLoadingManual(false);
      }
    },
    onError: () => {
      setLoadingManual(false);
    },
  });

  const pesquisaSerie = useMutation({
    mutationFn: () => pesquisarSeriePorNome({ nome: serieNome }),
    onSuccess: (response) => {
      if (!navegarComResultadoElenco(response.data)) {
        setLoadingManual(false);
      }
    },
    onError: () => {
      setLoadingManual(false);
    },
  });

  const pesquisaFilme = useMutation({
    mutationFn: () => pesquisarFilmePorNome({ nome: serieNome }),
    onSuccess: (response) => {
      if (!navegarComResultadoElenco(response.data)) {
        setLoadingManual(false);
      }
    },
    onError: () => {
      setLoadingManual(false);
    },
  });

  const pesquisando =
    pesquisaAtor.isPending ||
    pesquisaSerie.isPending ||
    pesquisaFilme.isPending;

  const exibirLoading = loadingManual || pesquisando;

  const handleSubmitAtor = (event) => {
    event.preventDefault();

    setFraseLoading("Pesquisando...");

    pesquisaAtor.mutate();
  };

  const handleSubmitElenco = (event) => {
    event.preventDefault();

    setFraseLoading("Pesquisando...");

    if (tipo === "MOVIE") {
      pesquisaFilme.mutate();
      return;
    }

    pesquisaSerie.mutate();
  };

  return (
    <div className="container-home">
      <Logo
        className={!authChecked ? "logo-image-loading" : "logo-image-ready"}
      />

      {!authChecked ? (
        <HomeLoading />
      ) : (
        <>
          <MenuUser isAuthenticated={isAuthenticated} />

          {tab === "PESSOA" ? (
            <PesquisaAtor handleSubmit={handleSubmitAtor} onChange={mudanca} />
          ) : tab === "ELENCO" ? (
            <div className="pesquisa-elenco">
              <PesquisaElenco
                handleSubmit={handleSubmitElenco}
                onChange={changeSerie}
                tab={tipo}
              />

              <Tab
                setTab={setTipo}
                estado={tipo}
                tabs={["TV", "MOVIE"]}
                tamanho="pequeno"
              />
            </div>
          ) : (
            <TirarFoto
              isAuthenticated={isAuthenticated}
              setLoading={setLoadingManual}
              setFraseLoading={setFraseLoading}
            />
          )}

          <div className="menu">
            <Tab
              setTab={setTab}
              estado={tab}
              tabs={["FOTO", "PESSOA", "ELENCO"]}
              tamanho="normal"
            />
          </div>
        </>
      )}

      {exibirLoading ? <Loading frase={fraseLoading} /> : null}
    </div>
  );
}
