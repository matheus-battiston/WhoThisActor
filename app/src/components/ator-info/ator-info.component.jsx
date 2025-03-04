import React, { useEffect, useState } from "react";
import "./ator-info.css";
import padrao from "../../assets/images/image-not-found.jpg";

export default function AtorInfo({ nome, imagem }) {
  const [foto, setFoto] = useState(null);

  useEffect(() => {
    if (imagem) {
      setFoto(imagem);
    } else {
      setFoto(padrao);
    }
  }, [imagem]);

  return (
    <div className="linhaAtor">
      <img
        src={foto}
        alt={nome}
        className="image-ator"
        onError={(e) => (e.target.src = padrao)}
      />
      <div className="producaoTexto">
        <h2 className="titleAtor">{nome}</h2>
      </div>
    </div>
  );
}
