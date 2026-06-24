import React from "react";
import "./ator-info.css";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";

export default function AtorInfo({
  nome,
  imagem,
  logado,
  favoritado,
  favoritar,
  favoritoPendente = false,
}) {
  const foto = imagem || undefined;

  return (
    <div className="linhaAtor">
      <div className="poster-ator-container">
        <LoadingImage
          src={foto}
          alt={nome}
          className="poster-ator-image"
          imgClassName="image-ator-info"
        />
        <div className="poster-ator-favorito">
          {logado ? (
            <FavoritoIcon
              ativo={favoritado}
              onClick={favoritar}
              disabled={favoritoPendente}
            />
          ) : null}
        </div>
      </div>
      <div className="producaoTexto">
        <h2 className="titleAtor">{nome}</h2>
      </div>
    </div>
  );
}
