import "./exibir-producoes.css";

import React, { useState } from "react";
import { useParams } from "react-router-dom";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import { useFavoritoAtor } from "../../api/hooks/use-favorito-ator/use-favorito-ator.hook";
import Loading from "../../components/loading/loading.component";
import Erro from "../../components/erro/erro.component";
import { usePesquisarAtorPorId } from "../../api/generated/api";
import { getErrorMessage } from "../../utils/getErrorMessage";
import ExibirProducoesMobileLayout from "./layouts/exibir-producoes-mobile.layout";
import ExibirProducoesWebLayout from "./layouts/exibir-producoes-web.layout";

export function ExibirProducoesScreen() {
  const FRASE_CARREGAMENTO = "Obtendo informações do ator";
  const { id } = useParams();
  const { isAuthenticated, authChecked } = useAuth();
  const isMobile = useIsMobile();

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

  const layoutProps = {
    ator: pesquisaAtor.data.data,
    tab,
    setTab,
    isAuthenticated,
    favoritado: favoritoLocal,
    favoritoPendente,
    favoritar: alternarFavorito,
  };

  return (
    <div className="container-producoes">
      {isMobile ? (
        <ExibirProducoesMobileLayout {...layoutProps} />
      ) : (
        <ExibirProducoesWebLayout {...layoutProps} />
      )}
    </div>
  );
}
