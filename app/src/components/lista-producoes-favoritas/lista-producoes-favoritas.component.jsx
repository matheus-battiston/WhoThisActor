import "./lista-producoes-favoritas.css";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import { useNavigate } from "react-router-dom";
import React from "react";
import { LoadingImage } from "../loading-image/loading-image.component";

export default function ListaProducoesFavoritasComponent({
  itens,
  tipo,
  tipoMidia,
}) {
  const navigate = useNavigate();

  function exibeProducao() {
    return (
      <div className="container-producoes-favoritas">
        {itens?.map((producao, index) => (
          <div
            key={index}
            className="producao-favorita"
            onClick={() => verProducao(producao)}
          >
            <LoadingImage
              src={
                producao.imagem
                  ? URL_BASE_IMAGEM_TMDB + producao.imagem
                  : undefined
              }
              alt={producao.nome}
              className="imagem-producao-favorita-wrapper imagem-card-favorito-wrapper"
              imgClassName="imagem-producao-favorita imagem-card-favorito"
            />
            <span className="titulo-producao-favorita">{producao.nome}</span>
          </div>
        ))}
      </div>
    );
  }

  function exibeAtor() {
    return (
      <div className="container-producoes-favoritas">
        {itens?.map((ator, index) => (
          <div
            key={index}
            className="producao-favorita producao-favorita-ator"
            onClick={() => verAtor(ator)}
          >
            <LoadingImage
              src={
                ator.urlImagem
                  ? URL_BASE_IMAGEM_TMDB + ator.urlImagem
                  : undefined
              }
              alt={ator.nome}
              className="imagem-producao-favorita-wrapper imagem-card-favorito-wrapper"
              imgClassName="imagem-producao-favorita imagem-card-favorito"
            />
            <span className="titulo-producao-favorita">{ator.nome}</span>
          </div>
        ))}
      </div>
    );
  }

  function verAtor(ator) {
    navigate(`/ator/${ator.id}`);
  }

  function verProducao(producao) {
    navigate(`/exibirElenco/${tipoMidia}/${producao.id}`);
  }

  return <>{tipo === "PRODUÇÃO" ? exibeProducao() : exibeAtor()}</>;
}
