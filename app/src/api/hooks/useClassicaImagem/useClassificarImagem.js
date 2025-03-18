import { useState } from "react";
import { classificar } from "../../calls/classificar.api";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { useNavigate } from "react-router-dom";

export function useClassificarImagem() {
  const [atores, setAtores] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  async function classificarImagemFunc(url, fast) {
    try {
      const respostaApi = await classificar(url, fast);
      setAtores(respostaApi);
    } catch (error) {
      dispatch(setError(error.response.data || "Erro desconhecido"));
      navigate("/");
    }
  }
  return { atores, classificarImagemFunc };
}
