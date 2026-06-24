import React, { useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./opcoes-atores.css";
import AtorInfo from "../../components/ator-info/ator-info.component";

export function OpcoesAtoresScreen() {
  const location = useLocation();
  const { opcoes } = location.state || {};

  const navigate = useNavigate();
  const navigateToItemDetails = useCallback(
    (id) => {
      navigate(`/ator/${id}`);
    },
    [navigate],
  );

  const handlePress = (item) => {
    navigateToItemDetails(item.id);
  };

  return (
    <div className="containerOpcoesAtores">
      <div className="opcoes">
        {opcoes?.map((item, index) => (
          <div
            key={`${item.identity}-${index}`}
            className="item"
            onClick={() => handlePress(item)}
          >
            <AtorInfo nome={item.identity} imagem={item.imagem} />
          </div>
        ))}
      </div>
      )
    </div>
  );
}
