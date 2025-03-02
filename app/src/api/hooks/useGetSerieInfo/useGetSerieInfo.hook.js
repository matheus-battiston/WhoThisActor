import { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { getSerieInfo } from "../../calls/getSerieInfo.api";

export function useGetSerieInfo() {
  const [serie, setSerie] = useState(null);
  const dispatch = useDispatch();

  // Utilizando useCallback para memorizar a função
  const getSerieInfoFunc = useCallback(
    async (nome, tipo) => {
      try {
        const respostaApi = await getSerieInfo(nome, tipo);
        setSerie(respostaApi);
      } catch (error) {
        console.log(error.response.data.message);
        dispatch(setError(error.response.data.message || "Erro desconhecido"));
      }
    },
    [dispatch]
  ); // O useCallback depende do dispatch, então o incluímos aqui.

  return { serie, getSerieInfoFunc };
}
