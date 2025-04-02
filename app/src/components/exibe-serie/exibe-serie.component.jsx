import "./exibe-serie.css";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/imageTmdb";

export default function ExibeSerie({ nome, imagem, providers }) {
  return (
    <div className="linha-serie">
      <img src={imagem} alt={nome} className="poster-serie" />
      <div className="texto-producao">
        <text className="titulo-producao">{nome}</text>
        <div className="providers">
          {providers?.map((provider, index) => (
            <img
              key={index}
              src={URL_BASE_IMAGEM_TMDB + provider.logo_path}
              alt={provider.nome}
              className="provider-logo"
            />
          ))}
        </div>
      </div>
    </div>
  );
}
