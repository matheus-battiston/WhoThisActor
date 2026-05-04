import { useCallback, useEffect, useState } from "react";
import {
  useDesfavoritarAtor,
  useFavoritarAtor,
} from "../../generated/api";

export function useFavoritoAtor({
  atorId,
  isAuthenticated,
  favoritadoInicial,
}) {
  const favoritarAtor = useFavoritarAtor();
  const desfavoritarAtor = useDesfavoritarAtor();

  const [favoritoLocal, setFavoritoLocal] = useState(false);
  const [atorFavoritoCarregadoId, setAtorFavoritoCarregadoId] = useState(null);

  const favoritoPendente =
    favoritarAtor.isPending || desfavoritarAtor.isPending;

  useEffect(() => {
    if (!isAuthenticated || atorId == null) {
      setFavoritoLocal(false);
      setAtorFavoritoCarregadoId(null);
      return;
    }

    setFavoritoLocal(Boolean(favoritadoInicial));
    setAtorFavoritoCarregadoId(atorId);
  }, [atorId, favoritadoInicial, isAuthenticated]);

  const alternarFavorito = useCallback(async () => {
    if (!isAuthenticated || atorId == null || favoritoPendente) return;

    const estadoAnterior = favoritoLocal;
    const proximoEstado = !favoritoLocal;

    setFavoritoLocal(proximoEstado);

    try {
      if (proximoEstado) {
        await favoritarAtor.mutateAsync({ id: atorId });
      } else {
        await desfavoritarAtor.mutateAsync({ id: atorId });
      }
    } catch (error) {
      setFavoritoLocal(estadoAnterior);
    }
  }, [
    atorId,
    desfavoritarAtor,
    favoritarAtor,
    favoritoLocal,
    favoritoPendente,
    isAuthenticated,
  ]);

  return {
    favoritoLocal,
    favoritoPendente,
    favoritoCarregado:
      !isAuthenticated ||
      (atorId != null && atorFavoritoCarregadoId === atorId),
    alternarFavorito,
  };
}
