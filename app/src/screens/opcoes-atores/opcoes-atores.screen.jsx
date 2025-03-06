import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import "./opcoes-atores.css";
import { useClassificarImagem } from "../../api/hooks/useClassicaImagem/useClassificarImagem";
import AtorInfo from "../../components/ator-info/ator-info.component";
import Loading from "../../components/loading/loading.component";
import { useCallback } from "react";

export function OpcoesAtoresScreen() {
  const url = useSelector((state) => state.url.url);
  const navigate = useNavigate();
  const { atores, classificarImagemFunc } = useClassificarImagem();
  const [loading, setLoading] = useState(true);
  const [loadingApi, setLoadingApi] = useState(false);

  const navigateToItemDetails = useCallback(
    (name) => {
      navigate(`/exibirProducoes/${name}`);
    },
    [navigate]
  );

  useEffect(() => {
    if (atores) {
      if (atores.length === 1) {
        navigateToItemDetails(atores[0].identity);
      }
      setLoading(false);
    }
  }, [atores, navigateToItemDetails]);

  useEffect(() => {
    if (url && !loadingApi) {
      setLoadingApi(true);
      classificarImagemFunc(url);
    }
  }, [url, classificarImagemFunc, loadingApi]);

  const handlePress = (item) => {
    navigateToItemDetails(item.identity);
  };

  return (
    <div className="containerOpcoesAtores">
      {loading ? (
        <Loading />
      ) : (
        <div className="opcoes">
          {atores.map((item) => (
            <div
              key={item.identity}
              className="item"
              onClick={() => handlePress(item)}
            >
              <AtorInfo nome={item.identity} imagem={item.imagem} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default OpcoesAtoresScreen;
