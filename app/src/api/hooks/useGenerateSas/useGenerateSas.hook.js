import { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { generateSas } from "../../calls/generateSas.api";
import { setError } from "../../../redux/store";
import { useNavigate } from "react-router-dom";

export function useGenerateSas() {
  const [sas, setSas] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const generateSasFunc = useCallback(
    async (callback) => {
      try {
        const respostaApi = await generateSas();
        setSas(respostaApi);

        if (callback) {
          callback(null, respostaApi);
        }
      } catch (error) {
        dispatch(setError("Erro ao se comunicar com o servidor"));
        navigate("/");
        if (callback) {
          callback(error, null);
        }
      }
    },
    [dispatch, navigate]
  );

  return { sas, generateSasFunc };
}
