import { useState, useCallback } from "react";
import { getProducoesporNome } from "../../calls/getProducoesPorNome.api";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { useNavigate } from "react-router-dom"; // Importando useNavigate

export function useGetProducoesPorNome() {
  const [producoes, setProducoes] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  // Utilizando useCallback para memorizar a função
  const getProducoesPorNomeFunc = useCallback(
    async (nome) => {
      try {
        const respostaApi = await getProducoesporNome(nome);
        setProducoes(respostaApi);
      } catch (error) {
        console.log("errroooooo");
        dispatch(setError(error.response.data || "Erro desconhecido"));
        navigate("/");
      }
    },
    [dispatch]
  ); // O useCallback depende do dispatch, então o incluímos aqui.

  return { producoes, getProducoesPorNomeFunc };
}
