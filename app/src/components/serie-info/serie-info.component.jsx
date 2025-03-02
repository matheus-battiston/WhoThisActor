import ExibeSerie from "../exibe-serie/exibe-serie.component";
import "./serie-info.css";

export function SerieInfo({ imagem, nome }) {
  return (
    <div className="container-producao">
      <ExibeSerie imagem={imagem} nome={nome} />
    </div>
  );
}

export default SerieInfo;
