import { useCallback, useEffect, useState } from "react";
import {
  useDesfavoritarFilme,
  useDesfavoritarSerie,
  useFavoritarFilme,
  useFavoritarSerie,
} from "../../generated/api";

export function useFavoritoProducao(
  producaoId,
  tipoMidia,
  isAuthenticated,
  favoritadoInicial,
) {
  const favoritarSerie = useFavoritarSerie();
  const desfavoritarSerie = useDesfavoritarSerie();
  const favoritarFilme = useFavoritarFilme();
  const desfavoritarFilme = useDesfavoritarFilme();

  const [favoritoLocal, setFavoritoLocal] = useState(
    Boolean(favoritadoInicial),
  );

  const favoritoPendente =
    favoritarSerie.isPending ||
    desfavoritarSerie.isPending ||
    favoritarFilme.isPending ||
    desfavoritarFilme.isPending;

  useEffect(() => {
    if (!isAuthenticated || producaoId == null || !tipoMidia) {
      setFavoritoLocal(false);
      return;
    }

    setFavoritoLocal(Boolean(favoritadoInicial));
  }, [favoritadoInicial, isAuthenticated, producaoId, tipoMidia]);

  const alternarFavorito = useCallback(async () => {
    if (!isAuthenticated || producaoId == null || !tipoMidia || favoritoPendente)
      return;

    const estadoAnterior = favoritoLocal;
    const proximoEstado = !estadoAnterior;

    setFavoritoLocal(proximoEstado);

    try {
      if (tipoMidia === "MOVIE") {
        if (proximoEstado) {
          await favoritarFilme.mutateAsync({ id: producaoId });
        } else {
          await desfavoritarFilme.mutateAsync({ id: producaoId });
        }
      } else {
        if (proximoEstado) {
          await favoritarSerie.mutateAsync({ id: producaoId });
        } else {
          await desfavoritarSerie.mutateAsync({ id: producaoId });
        }
      }
    } catch (error) {
      setFavoritoLocal(estadoAnterior);
      console.error(error);
    }
  }, [
    desfavoritarFilme,
    desfavoritarSerie,
    favoritarFilme,
    favoritarSerie,
    favoritoLocal,
    favoritoPendente,
    isAuthenticated,
    producaoId,
    tipoMidia,
  ]);

  return {
    favoritoLocal,
    favoritoPendente,
    favoritoCarregado: true,
    alternarFavorito,
  };
}
