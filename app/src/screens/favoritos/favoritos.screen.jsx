import React, { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import {
  getListarAtoresFavoritosQueryKey,
  getListarProducoesFavoritasQueryKey,
  useDesfavoritarAtor,
  useDesfavoritarFilme,
  useDesfavoritarSerie,
  useListarAtoresFavoritos,
  useListarProducoesFavoritas,
} from "../../api/generated/api";
import FavoritosMobileLayout from "./layouts/favoritos-mobile.layout";
import FavoritosWebLayout from "./layouts/favoritos-web.layout";

export default function FavoritosScreen() {
  const [aba, setAba] = useState("PRODUCOES");
  const [tipoProducao, setTipoProducao] = useState("series");
  const queryClient = useQueryClient();
  const isMobile = useIsMobile();

  const atoresFavoritos = useListarAtoresFavoritos();
  const producoesFavoritas = useListarProducoesFavoritas();
  const desfavoritarAtor = useDesfavoritarAtor({
    mutation: {
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: getListarAtoresFavoritosQueryKey(),
        });
      },
    },
  });
  const desfavoritarSerie = useDesfavoritarSerie({
    mutation: {
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: getListarProducoesFavoritasQueryKey(),
        });
      },
    },
  });
  const desfavoritarFilme = useDesfavoritarFilme({
    mutation: {
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: getListarProducoesFavoritasQueryKey(),
        });
      },
    },
  });

  const loading = atoresFavoritos.isLoading || producoesFavoritas.isLoading;
  const producoes = producoesFavoritas.data?.data || {};
  const atores = atoresFavoritos.data?.data || [];
  const listaProducoes =
    tipoProducao === "filmes" ? producoes.filmes || [] : producoes.series || [];
  const removendoFavorito =
    desfavoritarAtor.isPending ||
    desfavoritarSerie.isPending ||
    desfavoritarFilme.isPending;

  const removerProducaoFavorita = (producao) => {
    if (!producao.id) return;

    if (tipoProducao === "filmes") {
      desfavoritarFilme.mutate({ id: producao.id });
      return;
    }

    desfavoritarSerie.mutate({ id: producao.id });
  };

  const removerAtorFavorito = (ator) => {
    if (!ator.id) return;

    desfavoritarAtor.mutate({ id: ator.id });
  };

  const layoutProps = {
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
  };

  return isMobile ? (
    <FavoritosMobileLayout {...layoutProps} />
  ) : (
    <FavoritosWebLayout {...layoutProps} />
  );
}
