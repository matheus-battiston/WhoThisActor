import React from "react";
import { useLocation } from "react-router-dom";
import ExibeProducao from "../../components/exibe-producao/exibe-producao.component";
import "./opcoes-producoes.css";

export function OpcoesProducoesScreen() {
  const location = useLocation();
  const { opcoes, tipoMidia } = location.state || {};

  const mapearProducao = (item) => {
    return {
      id: item.id,
      nomeProducao: item.nome,
      posterLink: item.imagem,
      nomePersonagem: "",
    };
  };

  const mapearTipo = (item) =>
    (item.tipoMidia || tipoMidia) === "MOVIE" ? "FILME" : "SERIE";

  return (
    <div className="containerOpcoesProducoes">
      <div className="opcoesProducoes">
        {opcoes?.map((item, index) => (
          <ExibeProducao
            key={`${item.id}-${index}`}
            producao={mapearProducao(item)}
            tipo={mapearTipo(item)}
          />
        ))}
      </div>
    </div>
  );
}
