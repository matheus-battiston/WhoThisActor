import { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { getSerieInfo } from "../../calls/getSerieInfo.api";
import { useNavigate } from "react-router-dom"; // Importando useNavigate

export function useGetSerieInfo() {
  const [serie, setSerie] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  // Utilizando useCallback para memorizar a função
  const getSerieInfoFunc = useCallback(
    async (nome, tipo) => {
      try {
        const respostaApi = await getSerieInfo(nome, tipo);
        setSerie(respostaApi);
      } catch (error) {
        dispatch(setError(error.response.data || "Erro desconhecido"));
        navigate("/");
      }
    },
    [dispatch]
  ); // O useCallback depende do dispatch, então o incluímos aqui.

  return { serie, getSerieInfoFunc };
}
