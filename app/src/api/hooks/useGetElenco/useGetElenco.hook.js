import { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { getElenco } from "../../calls/getElenco.api";
import { useNavigate } from "react-router-dom";

export function useGetElenco() {
  const [pessoas, setPessoas] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const getElencoFunc = useCallback(
    async (nome, personagem, tipo) => {
      try {
        const respostaApi = await getElenco(nome, personagem, tipo);
        setPessoas(respostaApi);
      } catch (error) {
        dispatch(setError(error.response.data || "Erro desconhecido"));
        navigate("/");
      }
    },
    [dispatch, navigate]
  );

  return { pessoas, getElencoFunc };
}
