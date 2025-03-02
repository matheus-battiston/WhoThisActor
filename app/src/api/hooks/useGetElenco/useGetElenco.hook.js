import { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { getElenco } from "../../calls/getElenco.api";

export function useGetElenco() {
  const [pessoas, setPessoas] = useState(null);
  const dispatch = useDispatch();

  // Utilizando useCallback para memorizar a função
  const getElencoFunc = useCallback(
    async (nome, personagem, tipo) => {
      try {
        const respostaApi = await getElenco(nome, personagem, tipo);
        setPessoas(respostaApi);
      } catch (error) {
        dispatch(setError(error.response.data || "Erro desconhecido"));
      }
    },
    [dispatch]
  ); // O useCallback depende do dispatch, então o incluímos aqui.

  return { pessoas, getElencoFunc };
}
