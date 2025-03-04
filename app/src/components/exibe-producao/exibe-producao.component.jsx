import React from "react";
import "./exibe-producao.css";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/imageTmdb";

export default function ExibeProducao({ producao, onImageLoad }) {
  function criaLink(link) {
    const url = `${URL_BASE_IMAGEM_TMDB}${link}`;
    return url;
  }

  return (
    <div className="containerExibeProducao">
      <div className="linha">
        <img
          src={criaLink(producao.posterLink)}
          alt={producao.nomeProducao}
          className="image"
          onLoad={onImageLoad}
        />
        <div className="producaoTexto">
          <h2 className="titleProducao">{producao.nomeProducao}</h2>
          <p className="personagem">{producao.nomePersonagem}</p>
        </div>
      </div>
    </div>
  );
}
