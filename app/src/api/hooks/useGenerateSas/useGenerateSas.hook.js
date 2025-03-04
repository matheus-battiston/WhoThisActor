import { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { generateSas } from "../../calls/generateSas.api";
import { setError } from "../../../redux/store";
import { useNavigate } from "react-router-dom"; // Importando useNavigate

export function useGenerateSas() {
  const [sas, setSas] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  // Usando useCallback para memorizar a função
  const generateSasFunc = useCallback(
    async (callback) => {
      try {
        const respostaApi = await generateSas();
        setSas(respostaApi);

        // Chama o callback passando o sucesso
        if (callback) {
          callback(null, respostaApi); // Passa os dados de resposta no sucesso
        }
      } catch (error) {
        dispatch(setError("Erro ao enviar a imagem ao servidor"));
        navigate("/");
        // Chama o callback passando o erro
        if (callback) {
          callback(error, null); // Passa o erro no caso de falha
        }
      }
    },
    [dispatch]
  ); // Apenas recria a função quando o dispatch mudar

  return { sas, generateSasFunc };
}
