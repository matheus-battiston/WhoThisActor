import { useState, useCallback } from "react";
import { getProducoesporNome } from "../../calls/getProducoesPorNome.api";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";

export function useGetProducoesPorNome() {
  const [producoes, setProducoes] = useState(null);
  const dispatch = useDispatch();

  // Utilizando useCallback para memorizar a função
  const getProducoesPorNomeFunc = useCallback(
    async (nome) => {
      try {
        const respostaApi = await getProducoesporNome(nome);
        setProducoes(respostaApi);
      } catch (error) {
        console.log(error.response.data.message);
        dispatch(setError(error.response.data.message || "Erro desconhecido"));
      }
    },
    [dispatch]
  ); // O useCallback depende do dispatch, então o incluímos aqui.

  return { producoes, getProducoesPorNomeFunc };
}
