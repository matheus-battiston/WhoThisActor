import { useState } from "react";
import { enviarImagemBlob } from "../../calls/enviarImagemBlob.api";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";
import { useNavigate } from "react-router-dom"; // Importando useNavigate

export function useEnviarImagemBlob() {
  const [blob, setBlob] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  async function enviarImagemBlobFunc(blobUrl, binaryData) {
    try {
      const respostaApi = await enviarImagemBlob(blobUrl, binaryData);
      setBlob(respostaApi);
    } catch (error) {
      dispatch(setError("Erro ao enviar a imagem para o servidor"));
      navigate("/");
    }
  }
  return { blob, enviarImagemBlobFunc };
}
