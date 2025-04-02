import ExibeSerie from "../exibe-serie/exibe-serie.component";
import "./serie-info.css";

export function SerieInfo({ imagem, nome, providers }) {
  return (
    <div className="container-producao">
      <ExibeSerie imagem={imagem} nome={nome} providers={providers} />
    </div>
  );
}

export default SerieInfo;
