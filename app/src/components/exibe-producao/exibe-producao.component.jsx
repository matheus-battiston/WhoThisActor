import React, { useEffect } from "react";
import "./exibe-producao.css";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/imageTmdb";
import { useNavigate } from "react-router-dom";

export default function ExibeProducao({ producao, onImageLoad, tipo }) {
  const navigate = useNavigate();
  const TIPO_STRING = tipo === "FILME" ? "MOVIE" : "TV";

  function verProducao() {
    navigate(
      `/exibirElenco/${encodeURIComponent(
        producao.nomeProducao
      )}/${TIPO_STRING}`
    );
  }

  function criaLink(link) {
    const url = `${URL_BASE_IMAGEM_TMDB}${link}`;
    return url;
  }

  return (
    <div className="container-exibe-producao" onClick={verProducao}>
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
