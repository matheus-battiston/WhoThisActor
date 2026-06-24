import React from "react";
import "./exibe-producao.css";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import { useNavigate } from "react-router-dom";
import { LoadingImage } from "../loading-image/loading-image.component";

export default function ExibeProducao({ producao, tipo }) {
  const navigate = useNavigate();

  function verProducao() {
    const tipoMidia = tipo === "FILME" ? "MOVIE" : "TV";
    navigate(`/exibirElenco/${tipoMidia}/${producao.id}`);
  }

  function criaLink(link) {
    return `${URL_BASE_IMAGEM_TMDB}${link}`;
  }

  return (
    <div className="container-exibe-producao" onClick={verProducao}>
      <div className="linha">
        <LoadingImage
          src={criaLink(producao.posterLink)}
          alt={producao.nomeProducao}
          className="image-wrapper"
          imgClassName="image"
        />

        <div className="producaoTexto">
          <h2 className="titleProducao" title={producao.nomeProducao}>
            {producao.nomeProducao}
          </h2>
          <p className="personagem" title={producao.nomePersonagem}>
            {producao.nomePersonagem}
          </p>
        </div>
      </div>
    </div>
  );
}
