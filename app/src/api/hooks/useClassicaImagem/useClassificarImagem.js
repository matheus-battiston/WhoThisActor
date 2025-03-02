import { useState } from "react";
import { classificar } from "../../calls/classificar.api";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";

export function useClassificarImagem() {
  const [atores, setAtores] = useState(null);
  const dispatch = useDispatch();

  async function classificarImagemFunc(url) {
    try {
      const respostaApi = await classificar(url);
      setAtores(respostaApi);
    } catch (error) {
      dispatch(setError(error.response.data.message || "Erro desconhecido"));
    }
  }
  return { atores, classificarImagemFunc };
}
