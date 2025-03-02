import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom"; // Importando o useNavigate para navegação
import "./opcoes-atores.css"; // Para aplicar os estilos
import { useClassificarImagem } from "../../api/hooks/useClassicaImagem/useClassificarImagem";
import AtorInfo from "../../components/ator-info/ator-info.component";
import Loading from "../../components/loading/loading.component";

export function OpcoesAtoresScreen() {
  const url = useSelector((state) => state.url.url);
  const navigate = useNavigate(); // Navegação no React Router
  const { atores, classificarImagemFunc } = useClassificarImagem();
  const [loading, setLoading] = useState(true);
  const [loadingApi, setLoadingApi] = useState(false);

  useEffect(() => {
    if (atores) {
      setLoading(false);
    }
  }, [atores]);

  useEffect(() => {
    if (url && !loadingApi) {
      setLoadingApi(true);
      classificarImagemFunc(url);
    }
  }, [url, classificarImagemFunc, loadingApi]);

  // Função a ser chamada ao clicar no item
  const handlePress = (item) => {
    navigateToItemDetails(item.identity);
  };

  const navigateToItemDetails = (name) => {
    navigate(`/exibirProducoes/${name}`); // Passando o parâmetro "name" via URL
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
