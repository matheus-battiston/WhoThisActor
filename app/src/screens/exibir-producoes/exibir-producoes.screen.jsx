import "./exibir-producoes.css";

import React, { useState } from "react";
import { useParams } from "react-router-dom";
import { useSwipeable } from "react-swipeable";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import { useFavoritoAtor } from "../../api/hooks/use-favorito-ator/use-favorito-ator.hook";
import Loading from "../../components/loading/loading.component";
import Erro from "../../components/erro/erro.component";
import Tab from "../../components/tab/tab.component";
import Cabecalho from "../../components/cabecalho/cabecalho.component";
import AtorInfo from "../../components/ator-info/ator-info.component";
import ListProducoes from "../../components/list-producoes/list-producoes.component";
import { usePesquisarAtorPorId } from "../../api/generated/api";
import { getErrorMessage } from "../../utils/getErrorMessage";

export function ExibirProducoesScreen() {
  const FRASE_CARREGAMENTO = "Obtendo informações do ator";
  const { id } = useParams();
  const { isAuthenticated, authChecked } = useAuth();

  const [tab, setTab] = useState("FILME");

  const pesquisaAtor = usePesquisarAtorPorId(Number(id), {
    query: { enabled: Boolean(id) },
  });

  const {
    favoritoLocal,
    favoritoPendente,
    favoritoCarregado,
    alternarFavorito,
  } = useFavoritoAtor({
    atorId: pesquisaAtor.data?.data.id,
    isAuthenticated,
    favoritadoInicial: pesquisaAtor.data?.data.favoritado,
  });

  const isLoading =
    !pesquisaAtor.isError &&
    (!authChecked || pesquisaAtor.isLoading || !favoritoCarregado);

  const handleSwipe = useSwipeable({
    onSwipedLeft: () => setTab("TV"),
    onSwipedRight: () => setTab("FILME"),
    trackMouse: true,
  });

  if (isLoading) {
    return (
      <div className="container-producoes">
        <Loading frase={FRASE_CARREGAMENTO} />
      </div>
    );
  }

  if (pesquisaAtor.isError) {
    return (
      <div className="container-producoes">
        <Erro message={getErrorMessage(pesquisaAtor.error)} />
      </div>
    );
  }

  return (
    <div className="container-producoes">
      <Cabecalho />

      <div className="ator-info-container">
        <AtorInfo
          nome={pesquisaAtor.data?.data.nome}
          imagem={pesquisaAtor.data?.data.urlFoto}
          logado={isAuthenticated}
          favoritado={favoritoLocal}
          favoritar={alternarFavorito}
          favoritoPendente={favoritoPendente}
        />
      </div>

      <Tab setTab={setTab} estado={tab} tabs={["FILME", "TV"]} />

      <ListProducoes
        producoes={pesquisaAtor.data.data}
        handleSwipe={handleSwipe}
        tab={tab}
      />
    </div>
  );
}
